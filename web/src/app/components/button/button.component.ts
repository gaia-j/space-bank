import {Component, Input} from "@angular/core";


@Component({
  selector: 'app-button',
  standalone: true,
  template: `
    <button>{{title}}</button>
  `,
  styleUrl: './button.component.css'
})

export class ButtonComponent {
  @Input() title: string = 'Button';
}
