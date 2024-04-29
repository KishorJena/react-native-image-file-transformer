package com.imagefiletransformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.imagefiletransformer.codec.DecoderFactory;
import com.imagefiletransformer.codec.EncoderFactory;
import com.imagefiletransformer.codec.MyDecoder;
import com.imagefiletransformer.codec.MyEncoder;
import com.imagefiletransformer.constants.FormatType;
import com.imagefiletransformer.constants.ScaleMode;
import com.imagefiletransformer.utility.BitmapUtility;
import com.imagefiletransformer.utility.FileUtility;
import com.imagefiletransformer.utility.MyUtility;
import com.imagefiletransformer.utility.StaticOptions;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageFileTransformerModule extends ReactContextBaseJavaModule {
  public static String TAG = ImageFileTransformerModule.class.getSimpleName();
  public static final String NAME = "ImageFileTransformer";
  // Assuming you have a class variable/member
  private final ExecutorService executorService = Executors.newSingleThreadExecutor();
//  private final ExecutorService executorService = Executors.newFixedThreadPool(4);

  private final Context context;

  public static String PARENT_DIR;
  public String KEY_DIR = "parentDir";
  public String CONTAINER_FOLDER_NAME = "tranformer";
  ImageFileTransformerModule(ReactApplicationContext context) {
    super(context);
    this.context = getReactApplicationContext();
    PARENT_DIR = getContainerDir().getAbsolutePath();
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @Override
  public Map<String, Object> getConstants() {

    final Map<String, Object> constants = new HashMap<>();
    Map<String, String> scale = ScaleMode.asMap();
    Map<String, String> format = FormatType.asMap();

    constants.put("Scale", scale);
    constants.put("Format", format);
    return constants;
  }

  private File getContainerDir(){
    // Get cache directory
    File cacheDir = context.getCacheDir();

    // Create a child folder called "myChildFolder"
    return new File(cacheDir, CONTAINER_FOLDER_NAME);
  }

  @ReactMethod
  public void StaticBatch(ReadableArray uris, ReadableMap userOptions, Promise promise) {
    StaticOptions options = new StaticOptions( "static", userOptions);

    // Create an array to store the paths of resized images
    WritableArray resizedPaths = Arguments.createArray();

    try {
      // temp fix
      overrideDestinationDir(userOptions);


      for (int i = 0; i < uris.size(); i++) {
        String uri = uris.getMap(i).getString("uri");

        File file = new File(uri);
        if (!file.exists()) continue;

        // create bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(uri);
        if(bitmap == null) continue;

        // re-scale
        Bitmap newBitmap = BitmapUtility.resizeBitmap(bitmap, options);
        bitmap.recycle();
        if(newBitmap == null) continue;

        Log.d(TAG,"1 ");
        // create file:  need extension
        File newFile = BitmapUtility.saveBitmapToFile(newBitmap, options);
        Log.d(TAG,"2 ");
        resizedPaths.pushString(newFile.getAbsolutePath());
        Log.d(TAG,"3 ");
      }
      Log.d(TAG,"5 ");

      promise.resolve(resizedPaths);
    } catch (Exception e) {
      promise.reject(TAG,TAG+" Error in ResizeStaticImagesBatch:", e);
    }
  }


  @ReactMethod
  public void AnimatedBatch(ReadableArray uris, ReadableMap userOptions, Promise promise) {

    StaticOptions options = new StaticOptions("animated", userOptions);

    WritableArray successUris = new WritableNativeArray();

//    AtomicInteger count = new AtomicInteger();
      try{

        // temp fix
        overrideDestinationDir(userOptions);

        // For each uri
        for(int i=0; i<uris.size(); i++){
          // Start Decode Encode
          String uri = process(uris.getString(i), options);

          // push if ok
          if(uri!=null){
            successUris.pushString(uri);
          }

//            count.incrementAndGet();
//            if(count.get() == uris.size()) {
//              // resolve with uris
//              promise.resolve(successUris);
//            }


        }
        promise.resolve(successUris);

      }catch (Exception e){
        Log.w(TAG,"e "+e.getMessage());
        // reject with error
        promise.reject(TAG,e);
      }



  }


  private String process(String uri, StaticOptions options) {

    // TODO check uri existence
    // Create output file
    File outputFile = FileUtility.createOutputFile(options.TARGET_FORMAT);

    try {
      // 1. Get resource : resource = GifDrawable / WebpDrawable
      Object resource = getResource(uri);
      // 2. Get decoder : decoder MyGifDecoder / MyWebpDecoder
      MyDecoder decoder = DecoderFactory.getDecoder(resource);
      // 3. Get encoder : encoder MyGifEncoder / MyWebpEncoder
      MyEncoder encoder = EncoderFactory.getEncoder(outputFile, options);

      boolean isBroke = false;
      // 4. Start Encoding...
      for(int i=0; i<decoder.getFrameCount(); i++){
        Bitmap ogBitmap = decoder.getNextFrame();
        Bitmap bitmap = BitmapUtility.resizeBitmap(ogBitmap, options );

        // if any fram is invalid then reject encoding
        if(bitmap == null){
          isBroke = true;
          break;
        }

        encoder.addFrame(bitmap, decoder.getNextDelay()); // TODO add delay
        bitmap.recycle();
        //NEXT
        decoder.advance();
      }
      // 5. Close Encoder
      encoder.close();
      if(isBroke) return null;

      // 6. Return result
      if(outputFile != null){
        return outputFile.getAbsolutePath();
      }
      return null;

    } catch (Exception e) {
      Log.d(TAG, "Exception in processing image"+e);

      // 6. inform rejection
      if (outputFile != null && outputFile.exists()) {
        if (outputFile.delete()) {
          Log.d(TAG, "File deleted successfully: " + outputFile.getAbsolutePath());
        } else {
          Log.w(TAG, "Failed to delete file: " + outputFile.getAbsolutePath());
        }
      }
      return null;
    }

  }

  private Object getResource(String uri) throws ExecutionException, InterruptedException {
    String inExtention = MimeTypeMap.getFileExtensionFromUrl(uri);

    // TODO : resusability
    if(inExtention.equalsIgnoreCase(FormatType.GIF.getType())){
      return Glide.with(this.context)
              .as(GifDrawable.class)
              .load(uri)
              .submit()
              .get();
    } else if(inExtention.equalsIgnoreCase(FormatType.WEBP.getType())){
      return Glide.with(this.context)
              .as(WebpDrawable.class)
              .load(uri)
              .submit()
              .get();
    }

    return null;
  }

  @ReactMethod
  public void clearCache(Promise promise){
      try{
        // delete
        FileUtility.deleteDirectory(getContainerDir());
        promise.resolve("Delected Successfully!, Deleted Dir: "+PARENT_DIR);
      }catch (Exception e){
        promise.reject(e);
      }
  }

  @ReactMethod
  public void getImageType(ReadableArray uris, Promise promise){
    WritableArray imageTypes = new WritableNativeArray();
    try{
      for (int i = 0; i < uris.size(); i++) {
        imageTypes.pushMap(getType(uris.getString(i)));
      }

      promise.resolve(imageTypes);
    }catch (Exception e){
      promise.reject(TAG,e);
    }
  }
  public WritableMap getType(String uri){
    File file = new File(uri);

    ArrayPool arrayPool = Glide.get(this.context).getArrayPool();
    List<ImageHeaderParser> parsers = Glide.get(this.context).getRegistry().getImageHeaderParsers();
    WritableMap map = new WritableNativeMap();
    // 1. uri
    map.putString("uri",uri);
    // 2. extention
    map.putString("extention", MyUtility.getImageExtention(uri));

    try(FileInputStream fis = new FileInputStream(file)){
      ImageHeaderParser.ImageType type = ImageHeaderParserUtils.getType(parsers, fis, arrayPool);

      // 2. type
      map.putString("type",type.name());

      // 3. isAnimated
      boolean isAnimated = MyUtility.isAnimated(type);
      map.putBoolean("isAnimated",isAnimated);

      // return map
      return map;
    }catch(Exception e){
      Log.w(TAG,"err "+e.getMessage());
      return map;
    }
  }

  private void overrideDestinationDir(ReadableMap userOptions) {

    if(userOptions.hasKey(KEY_DIR)) {

      String userDir = userOptions.getString(KEY_DIR);

      if(userDir != null) {

        File dir = new File(userDir);

        if(!dir.exists()) {
          Log.e(TAG, "User provided parentDir does not exist");
        }
        else if(!dir.canRead() || !dir.canWrite()) {
          Log.e(TAG, "Do not have read/write permissions for user provided parentDir");
        }
        else {
          PARENT_DIR = userDir;
        }

      }
    }
  }



}
