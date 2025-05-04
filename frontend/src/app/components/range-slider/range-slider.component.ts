import {Component, signal} from '@angular/core';
import {MatSlider, MatSliderModule, MatSliderRangeThumb} from '@angular/material/slider';
import {MatFormField} from '@angular/material/form-field';
import {MatInput, MatLabel} from '@angular/material/input';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-range-slider',
  imports: [
    MatSlider,
    MatSliderRangeThumb,
    MatFormField,
    MatLabel,
    MatInput,
    FormsModule,
  ],
  templateUrl: './range-slider.component.html',
  styleUrl: './range-slider.component.css'
})
export class RangeSliderComponent {
  minValue: number = 0;
  maxValue: number = 28000;

  leftValue = this.minValue;
  rightValue = this.maxValue;
}
