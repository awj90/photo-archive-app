import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppService } from '../app.service';
import { BeforeLeavingComponent } from '../utils';

@Component({
  selector: 'app-view-one',
  templateUrl: './view-one.component.html',
  styleUrls: ['./view-one.component.css'],
})
export class ViewOneComponent implements OnInit, BeforeLeavingComponent {
  constructor(private fb: FormBuilder, private appService: AppService) {}

  form!: FormGroup;

  @ViewChild('zipFile')
  zipFile!: ElementRef;

  ngOnInit(): void {
    this.form = this.createForm();
  }

  process() {
    const f: File = this.zipFile.nativeElement.files[0];
    this.appService.upload(
      this.form.value.name,
      this.form.value.title,
      this.form.value.comments,
      f
    );
    this.form = this.createForm();
  }

  formNotSaved(): boolean {
    return this.form.dirty;
  }

  confirmMessage(): string {
    return 'You have not completed the upload.\n Are you sure you want to leave?';
  }

  private createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      title: this.fb.control<string>('', [Validators.required]),
      comments: this.fb.control<string>(''),
      archive: this.fb.control<File | null>(null, [Validators.required]),
    });
  }
}
