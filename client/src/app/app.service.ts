import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { firstValueFrom, lastValueFrom } from 'rxjs';
import { Archive, UploadSuccessResponse } from './models';
import { Router } from '@angular/router';

@Injectable()
export class AppService {
  http = inject(HttpClient);
  router = inject(Router);

  API_URL: string = 'https://lazy-insect-production.up.railway.app';
  // API_URL: string = 'http://localhost:8080';

  upload(name: string, title: string, comments: string, archive: File) {
    const formData = new FormData();
    formData.set('name', name);
    formData.set('title', title);
    formData.set('comments', comments);
    formData.set('archive', archive);

    lastValueFrom(
      this.http.post<UploadSuccessResponse>(`${this.API_URL}/upload`, formData)
    )
      .then((resp) => {
        this.router.navigate(['/bundle', resp.bundleId]);
      })
      .catch((err) => {
        alert(JSON.stringify(err));
      });
  }

  getBundleById(bundleId: string): Promise<Archive> {
    return firstValueFrom(
      this.http.get<Archive>(`${this.API_URL}/bundle/${bundleId}`)
    );
  }

  getBundles(): Promise<Archive[]> {
    return lastValueFrom(this.http.get<Archive[]>(`${this.API_URL}/bundles`));
  }
}
