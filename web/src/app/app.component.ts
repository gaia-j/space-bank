import {Component, inject} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {ButtonComponent} from "./components/button/button.component";
import {AuthPageComponent} from "./components/auth-page/auth-page.component";
import {HttpClientModule} from "@angular/common/http";
import {AngularSvgIconModule} from "angular-svg-icon";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    ButtonComponent,
    AuthPageComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})


export class AppComponent {
  title = 'web';

  router = inject(Router);

  ngOnInit(): void {
    this.router.navigate(['/auth']).then(r => console.log(r));
  }

}
