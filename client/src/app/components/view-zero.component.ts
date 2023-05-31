import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { Archive } from '../models';

@Component({
  selector: 'app-view-zero',
  templateUrl: './view-zero.component.html',
  styleUrls: ['./view-zero.component.css'],
})
export class ViewZeroComponent implements OnInit {
  constructor(private appService: AppService) {}

  archives$!: Promise<Archive[]>;

  ngOnInit(): void {
    this.archives$ = this.appService.getBundles();
  }
}
