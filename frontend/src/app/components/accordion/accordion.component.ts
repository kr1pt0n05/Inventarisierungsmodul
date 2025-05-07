import {Component, input, ViewChild} from '@angular/core';
import {MatAccordion, MatExpansionPanel, MatExpansionPanelHeader} from '@angular/material/expansion';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-accordion',
  imports: [
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelHeader
  ],
  templateUrl: './accordion.component.html',
  styleUrl: './accordion.component.css'
})
export class AccordionComponent {
  title = input("Example title");

  // Needed, so we can reference matAccordion from our parent element
  @ViewChild(MatAccordion, {static: true}) matAccordion!: MatAccordion;
}
