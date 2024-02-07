# react-native-image-file-transformer

Scale, Convert & Compress animated & static image files

[ ⚠️ Under Developmenet ]
## Installation

```sh
npm install react-native-image-file-transformer
```

## Usage

```js
import { Transform } from 'react-native-image-file-transformer';

// ...

await Transform.static(uri, options);
await Transform.animated(uri, options);
await Transform.static([uri2, uri2, ], options);
await Transform.animated([uri1, uri2, ], options);

await Transform.getImageType(uri or [uri1, uri2, ...])
await Transform.clearCache()

Transform.ScaleMode // will give constants to use in options
Transform.FormatType // will give constants to use in options

Transform.animated(uris, { 
  width:100, // default will be taken from image
  height:100, // default will be taken from image
  quality:70,  // default 100
  mode:Transform.ScaleMode.FIT_CENTER, // default FIT_CENTER
  targetFormat:Transform.FormatType.AWEBP, // default JPGE/GIF
//   minDelay:80, (ms)
//   maxDelay:1000, (ms)
//   parentDir:"storage/emulated/0/Download/output"
})
.then(r=>{log(r)})
.catch(e=>{log('error ',e)})
```


## License

MIT

---

Feel free to contact for any query 
https://twitter.com/heyKSR