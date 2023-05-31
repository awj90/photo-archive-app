export interface UploadSuccessResponse {
  bundleId: string;
}

export interface Archive {
  bundleId: string;
  date: string;
  title: string;
  name: string;
  comments: string;
  urls: string[];
}
