import {Component} from '@angular/core';
import {
  MatDatepickerToggle,
  MatDateRangeInput,
  MatDateRangePicker
} from '@angular/material/datepicker';
import {MatFormField} from '@angular/material/form-field';
import {MatHint, MatLabel} from '@angular/material/input';

@Component({
  selector: 'app-datepicker',
  imports: [
    MatFormField,
    MatDateRangeInput,
    MatDatepickerToggle,
    MatDateRangePicker,
    MatHint,
    MatLabel
  ],
  templateUrl: './datepicker.component.html',
  styleUrl: './datepicker.component.css'
})
export class DatepickerComponent {

}
