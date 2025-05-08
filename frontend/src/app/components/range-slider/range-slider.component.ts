import {Component, input, signal} from '@angular/core';
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
  minValue = input<number>(0);
  maxValue = input<number>(100);

  leftValue = 0;
  rightValue = 1000;

  ngAfterViewInit() {
    this.leftValue = this.minValue();
    this.rightValue = this.maxValue();
  }
}
