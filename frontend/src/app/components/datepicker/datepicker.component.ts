import {ChangeDetectionStrategy, Component, input} from '@angular/core';
import {
  MatDatepickerModule,
  MatDatepickerToggle,
  MatDateRangeInput,
  MatDateRangePicker
} from '@angular/material/datepicker';
import {MatFormField, MatFormFieldModule} from '@angular/material/form-field';
import {MatHint, MatLabel} from '@angular/material/input';
import {provideNativeDateAdapter} from '@angular/material/core';
import {FormControl, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-datepicker',
  imports: [
    MatFormField,
    MatDateRangeInput,
    MatDatepickerToggle,
    MatDateRangePicker,
    MatHint,
    MatLabel,
    MatFormFieldModule,
    MatDatepickerModule,
    ReactiveFormsModule,
  ],
  templateUrl: './datepicker.component.html',
  styleUrl: './datepicker.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DatepickerComponent {
  readonly minDate = new Date(2000, 0, 1, 1, 1, 1, 1);
  readonly today = new Date();

  controlMin = input.required<FormControl>();
  controlMax = input.required<FormControl>();
}
