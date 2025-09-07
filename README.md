# React Bative Image File Transformer
## convert, compress, scale any animated or static image 


![image-removebg-preview1-ezgif com-resize (3)](https://github.com/KishorJena/react-native-image-file-transformer/assets/19212755/e8746a4a-9ae0-46ae-994b-b211d38c621b)

[![npm version](https://badge.fury.io/js/react-native-image-file-transformer.svg)](https://badge.fury.io/js/react-native-image-file-transformer)

`react-native-image-file-transformer` is a React Native module that provides functionality for transforming and manipulating image files. 

Platform support: 

- [x] Android
- [ ] ios

## Installation

```sh
npm install react-native-image-file-transformer
```

## Usage

Import:
```js
import { Transform } from 'react-native-image-file-transformer';
```

Operations:
```js
await Transform.static([uri2, uri2,... ], options);
await Transform.animated([uri1, uri2,... ], options);
```

Constants:
```js
ScaleMode:
Transform.ScaleMode.CROP
Transform.ScaleMode.STRETCH
Transform.ScaleMode.FIT_CENTER

FormatType:
Transform.FormatType.JPEG
Transform.FormatType.PNG
Transform.FormatType.WEBP
Transform.FormatType.GIF
Transform.FormatType.AWEBP
Transform.FormatType.UNKNOWN
 
```
Misc:
```js
await Transform.getImageType([uri1, uri2, ...])

// Output
//[{"extention": "png", "isAnimated": false, "type": "PNG_A", "uri": "/storage/emulated/..."}] 
```
```js
await Transform.clearCache()
```
## Example:
```js
Transform.animated(uris, { 
  width:100, // default will be taken from image
  height:100, // default will be taken from image
  quality:70,  // default 100
  scaleMode:Transform.ScaleMode.FIT_CENTER, // default FIT_CENTER
  targetFormat:Transform.FormatType.AWEBP, // default JPGE/GIF
//   minDelay:80, (ms)
//   maxDelay:1000, (ms)
//   parentDir:"storage/emulated/0/Download/output"
})
.then(r=>{log(r)})
.catch(e=>{log('error ',e)})
```

## Contribution
If you find any issues or have suggestions for improvements, please open an issue or submit a pull request on the project's GitHub repository. [CONTRIBUTING.md](CONTRIBUTING.md)

### Contact on:
X (twitter) : x.com/KishorJena01

## License

[LICENSE](LICENSE)
