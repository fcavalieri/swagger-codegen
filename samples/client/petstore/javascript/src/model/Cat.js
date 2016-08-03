/**
 * Swagger Petstore
 * This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\
 *
 * OpenAPI spec version: 1.0.0
 * Contact: apiteam@swagger.io
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function(root, factory) {
  if (typeof define === 'function' && define.amd) {
    // AMD. Register as an anonymous module.
    define(['ApiClient', 'model/Animal'], factory);
  } else if (typeof module === 'object' && module.exports) {
    // CommonJS-like environments that support module.exports, like Node.
    module.exports = factory(require('../ApiClient'), require('./Animal'));
  } else {
    // Browser globals (root is window)
    if (!root.SwaggerPetstore) {
      root.SwaggerPetstore = {};
    }
    root.SwaggerPetstore.Cat = factory(root.SwaggerPetstore.ApiClient, root.SwaggerPetstore.Animal);
  }
}(this, function(ApiClient, Animal) {
  'use strict';




  /**
   * The Cat model module.
   * @module model/Cat
   * @version 1.0.0
   */

  /**
   * Constructs a new <code>Cat</code>.
   * @alias module:model/Cat
   * @class
   * @extends module:model/Animal
   * @param className {String} 
   */
  var exports = function(className) {
    var _this = this;
    Animal.call(_this, className);

  };

  /**
   * Constructs a <code>Cat</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/Cat} obj Optional instance to populate.
   * @return {module:model/Cat} The populated <code>Cat</code> instance.
   */
  exports.constructFromObject = function(data, obj) {
    if (data) {
      obj = obj || new exports();
      Animal.constructFromObject(data, obj);
      if (data.hasOwnProperty('declawed')) {
        obj['declawed'] = ApiClient.convertToType(data['declawed'], 'Boolean');
      }
    }
    return obj;
  }

  exports.prototype = Object.create(Animal.prototype);
  exports.prototype.constructor = exports;

  /**
   * @member {Boolean} declawed
   */
  exports.prototype['declawed'] = undefined;



  return exports;
}));


