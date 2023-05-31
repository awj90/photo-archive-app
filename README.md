## Set/Export environment variables

- SECRET_KEY and ACCESS_KEY for Digital Ocean Space Bucket
- SPRING_DATA_MONGODB_URI

## POST /upload

- File input MIME: application/zip
- Upload Multipart Form data (images in Amazon S3, metadata in MongoDB)

## GET /bundles

- Fetch all albums

## GET /bundle/:bundleId

- Fetch images and metadata of a selected album
