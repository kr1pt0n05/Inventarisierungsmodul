import { Component } from '@angular/core';
import {CardComponent} from '../../components/card/card.component';
import {UploadService} from '../../services/upload.service';
import {MatButton} from '@angular/material/button';


interface File {
  file: any;
  name: string;
  size: number;
}

@Component({
  selector: 'app-import',
  imports: [
    CardComponent,
    MatButton
  ],
  templateUrl: './import.component.html',
  styleUrl: './import.component.css'
})
export class ImportComponent {

  constructor(private api: UploadService) {
  }

  file!: File | null;

  onFileSelected(event: any) {
    const file = event.target.files[0];
    this.file = {
      file: file,
      name: file.name,
      size: file.size,
    };
    console.log(this.file);
  }

  removeFile() {
    this.file = null;
  }

  upload() {
    if(this.file) this.api.uploadExcel(this.file.file).subscribe({
      next: (data) => {
        console.log(data);
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {
        console.log('complete');
      }
    });
  }

}
