// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,

  //bagtrackServiceEndpoint: 'http://localhost:8080',
  //mintoServiceEndpoint: 'http://localhost:8081',
  //flightDelayEndpoint: 'http://localhost:8082',
  //triggerPredictionEndpoint: 'http://localhost:3300'
  //triggerPredictionEndpoint: 'https://angular.io/'

  bagtrackServiceEndpoint: 'https://airlinemindtree.azurewebsites.net/airline-app',
  mintoServiceEndpoint: 'https://travelswift.azurewebsites.net/minto-app',
  flightDelayEndpoint: 'https://airlinemindtree.azurewebsites.net/flightdelay',
  triggerPredictionEndpoint: 'http://swiftvm.centralus.cloudapp.azure.com:3000',
  //triggerPredictionEndpoint: 'https://angular.io/'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
