import { Component, inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CardComponent } from '../../components/card/card.component';
import { ProcessingIndicatorComponent } from '../../components/processing-indicator/processing-indicator.component';
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
  readonly dialog = inject(MatDialog);

  constructor(private readonly api: UploadService) {
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
    if (this.file) {
      const dialogRef = this.dialog.open(ProcessingIndicatorComponent);
      this.api.uploadExcel(this.file.file).subscribe({
        next: (data) => {
          console.log(data);
        },
        error: (err) => {
          console.log(err);
          dialogRef.close();
          this._snackBar.open('Fehler beim Hochladen der Datei', 'Close', {
            duration: 3000,
          });
        },
        complete: () => {
          console.log('complete');
          dialogRef.close();
          this._snackBar.open('Datei erfolgreich hochgeladen', 'Close', {
            duration: 3000,
          });
          this.file = null;
        }
      });
    }
  }

}
