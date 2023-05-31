import { Component, OnInit } from '@angular/core';
import { Archive } from '../models';
import { AppService } from '../app.service';
import { ActivatedRoute } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-view-two',
  templateUrl: './view-two.component.html',
  styleUrls: ['./view-two.component.css'],
})
export class ViewTwoComponent implements OnInit {
  constructor(
    private appService: AppService,
    private activatedRoute: ActivatedRoute
  ) {}

  archive$!: Promise<Archive>;

  ngOnInit(): void {
    this.archive$ = this.appService.getBundleById(
      this.activatedRoute.snapshot.params['bundleId']
    );
  }
}
