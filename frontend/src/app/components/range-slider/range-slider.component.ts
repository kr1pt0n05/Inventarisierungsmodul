import {Component, effect, input, signal} from '@angular/core';
import {MatSlider, MatSliderRangeThumb} from '@angular/material/slider';
import {MatFormField} from '@angular/material/form-field';
import {MatInput, MatLabel} from '@angular/material/input';
import {FormControl, FormsModule, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-range-slider',
  imports: [
    MatSlider,
    MatSliderRangeThumb,
    MatFormField,
    MatLabel,
    MatInput,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './range-slider.component.html',
  styleUrl: './range-slider.component.css'
})
export class RangeSliderComponent {

  constructor() {
    effect(() => {
      this.controlMin().setValue(this.leftValue());
      this.controlMax().setValue(this.rightValue());
    });

    effect(() => {
      this.leftValue.set(this.minValue());
      this.rightValue.set(this.maxValue());
    });
  }

  minValue = input<number>(0);
  maxValue = input<number>(100);

  controlMin = input.required<FormControl>();
  controlMax = input.required<FormControl>();

  leftValue = signal<number>(0);
  rightValue = signal<number>(1000);

}
