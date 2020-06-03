(function () {
  try {
    return angular.module('bonitasoft.ui.widgets');
  } catch(e) {
    return angular.module('bonitasoft.ui.widgets', []);
  }
})().directive('customCustomBooleanMatrix', function() {
    return {
      controllerAs: 'ctrl',
      controller: /**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope) {

},
      template: '<!-- The custom widget template is defined here\n   - You can use standard HTML tags and AngularJS built-in directives, scope and interpolation system\n   - Custom widget properties defined on the right can be used as variables in a templates with properties.newProperty\n   - Functions exposed in the controller can be used with ctrl.newFunction()\n   - You can use the \'environment\' property injected in the scope when inside the Editor whiteboard. It allows to define a mockup\n     of the Custom Widget to be displayed in the whiteboard only. By default the widget is represented by an auto-generated icon\n     and its name (See the <span> below).\n-->\n<style>\nimg {\n  display: block;\n  margin-left: auto;\n  margin-right: auto;\n}\n</style>\n\n<style>\nth.rotate {\n  height: 100px;\n  white-space: nowrap;\n  margin-left: auto;\n  margin-right: auto;\n}\n\nth.rotate > div {\n  transform: \n    rotate(270deg)\n    translate(-20px,0px);\n  width: 30px;\n}\n</style>\n\n<table border="1">\n    <thead>\n    <tr>\n        <th></th>\n        <th ng-repeat="hheader in properties.hHeaders track by $index" class="rotate">\n            <div>{{hheader}}</div>\n        </th>\n    </tr>\n    </thead>\n    <tbody>\n    <tr ng-repeat="vheader in properties.vHeaders track by $index" class="row100">\n        <td>{{vheader}}</td>\n        <td ng-repeat="hheader in properties.hHeaders track by $index" class="column100">\n            <div ng-if="properties.values[$parent.$index * properties.hHeaders.length + $index] == \'true\'">\n                <img src="widgets/customCustomBooleanMatrix/assets/img/box_checked.svg"/>\n            </div>\n            <div ng-if="properties.values[$parent.$index * properties.hHeaders.length + $index] == \'false\'">\n                <img src="widgets/customCustomBooleanMatrix/assets/img/box_unchecked.svg"/>\n            </div>\n            <div ng-if="properties.values[$parent.$index * properties.hHeaders.length + $index] == \'unknown\'">\n                <img src="widgets/customCustomBooleanMatrix/assets/img/box_empty.svg"/>\n            </div>\n        </td>\n    </tr>\n    </tbody>\n</table>\n'
    };
  });
