import { Component, inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CardComponent } from '../../components/card/card.component';
import { UploadService } from '../../services/upload.service';


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

  private readonly _snackBar = inject(MatSnackBar);

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
    if (this.file) this.api.uploadExcel(this.file.file).subscribe({
      next: (data) => {
        console.log(data);
      },
      error: (err) => {
        console.log(err);
        this._snackBar.open('Error uploading file', 'Close', {
          duration: 3000,
        });
      },
      complete: () => {
        console.log('complete');
        this._snackBar.open('File uploaded successfully', 'Close', {
          duration: 3000,
        });
      }
    });
  }

}
