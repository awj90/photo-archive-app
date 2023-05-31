import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { ViewOneComponent } from './components/view-one.component';
import { ViewZeroComponent } from './components/view-zero.component';
import { ViewTwoComponent } from './components/view-two.component';
import { Routes, RouterModule } from '@angular/router';
import { AppService } from './app.service';

const appRoutes: Routes = [
  { path: 'bundles', component: ViewZeroComponent, title: 'Home' },
  { path: 'upload', component: ViewOneComponent, title: 'Upload your photos' },
  {
    path: 'bundle/:bundleId',
    component: ViewTwoComponent,
    title: 'Your photos',
  },
  { path: '**', redirectTo: '/bundles', pathMatch: 'full' },
];

@NgModule({
  declarations: [
    AppComponent,
    ViewOneComponent,
    ViewZeroComponent,
    ViewTwoComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
  ],
  providers: [AppService],
  bootstrap: [AppComponent],
})
export class AppModule {}
