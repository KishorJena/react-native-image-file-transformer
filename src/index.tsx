import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-image-file-transformer' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const ImageFileTransformerModule = isTurboModuleEnabled
  ? require('./NativeImageFileTransformer').default
  : NativeModules.ImageFileTransformer;

const ImageFileTransformer = ImageFileTransformerModule
  ? ImageFileTransformerModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

interface StaticOptions {
  width?: number;
  height?: number;
  outFormat?: string;
  mode?: string;
  quality?: number;
}

function staticImageBatch(uris:string | string[], userOptions: StaticOptions): Promise<void> {
  return new Promise((resolve, reject) => {
    const uriArray = Array.isArray(uris) ? uris : [uris];
    
    ImageFileTransformer.StaticBatch(uriArray, userOptions)
      .then((r) => {
        resolve(r);
      })
      .catch((error) => {
        reject(error);
      });
  });
}

function animatedImageBatch(uris:string | string[], userOptions: StaticOptions): Promise<void> {
  return new Promise((resolve, reject) => {
    const uriArray = Array.isArray(uris) ? uris : [uris];
    
    
    ImageFileTransformer.AnimatedBatch(uriArray, userOptions)
      .then((r) => {
        resolve(r);
      })
      .catch((error) => {
        reject(error);
      });
  });
}

function getImageType(uris: string | string[]): Promise<void> {
  return new Promise((resolve, reject) => {
    const uriArray = Array.isArray(uris) ? uris : [uris];

    ImageFileTransformer.getImageType(uriArray)
      .then((result) => {
        resolve(result);
      })
      .catch((error) => {
        reject(error);
      });
  });
}

// 
export const Transform = {
  static : staticImageBatch, 
  animated : animatedImageBatch,
  ScaleMode : ImageFileTransformer.getConstants().Scale,
  FormatType : ImageFileTransformer.getConstants().Format,
  getImageType : getImageType,
  clearCache : ImageFileTransformer.clearCache()
}


// TODO: REMOVE this
export default ImageFileTransformer;