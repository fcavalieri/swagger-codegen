//
// PetAPI.swift
//
// Generated by swagger-codegen
// https://github.com/swagger-api/swagger-codegen
//

import Alamofire
import PromiseKit

extension PetstoreClientAPI {
    
    public class PetAPI: APIBase {
    
        /**
         
         Update an existing pet
         
         - PUT /pet
         - 
         - OAuth:
           - type: oauth2
           - name: petstore_auth
         
         - parameter body: (body) Pet object that needs to be added to the store

         - returns: RequestBuilder<Void> 
         */
        public class func updatePet(body body: Pet?) -> RequestBuilder<Void> {
            let path = "/pet"
            let URLString = PetstoreClientAPI.basePath + path
            
            let parameters = body?.encodeToJSON() as? [String:AnyObject]

            let requestBuilder: RequestBuilder<Void>.Type = PetstoreClientAPI.requestBuilderFactory.getBuilder()

            return requestBuilder.init(method: "PUT", URLString: URLString, parameters: parameters, isBody: true)
        }
    
        /**
         
         Add a new pet to the store
         
         - POST /pet
         - 
         - OAuth:
           - type: oauth2
           - name: petstore_auth
         
         - parameter body: (body) Pet object that needs to be added to the store

         - returns: RequestBuilder<Void> 
         */
        public class func addPet(body body: Pet?) -> RequestBuilder<Void> {
            let path = "/pet"
            let URLString = PetstoreClientAPI.basePath + path
            
            let parameters = body?.encodeToJSON() as? [String:AnyObject]

            let requestBuilder: RequestBuilder<Void>.Type = PetstoreClientAPI.requestBuilderFactory.getBuilder()

            return requestBuilder.init(method: "POST", URLString: URLString, parameters: parameters, isBody: true)
        }
    
        /**
         
         Finds Pets by status
         
         - GET /pet/findByStatus
         - Multiple status values can be provided with comma seperated strings
         - OAuth:
           - type: oauth2
           - name: petstore_auth
         - examples: [{example=[ {
  "tags" : [ {
    "id" : 123456789,
    "name" : "aeiou"
  } ],
  "id" : 123456789,
  "category" : {
    "id" : 123456789,
    "name" : "aeiou"
  },
  "status" : "aeiou",
  "name" : "doggie",
  "photoUrls" : [ "aeiou" ]
} ], contentType=application/json}, {example=<Pet>
  <id>123456</id>
  <name>doggie</name>
  <photoUrls>
    <photoUrls>string</photoUrls>
  </photoUrls>
  <tags>
  </tags>
  <status>string</status>
</Pet>, contentType=application/xml}]
         - examples: [{example=[ {
  "tags" : [ {
    "id" : 123456789,
    "name" : "aeiou"
  } ],
  "id" : 123456789,
  "category" : {
    "id" : 123456789,
    "name" : "aeiou"
  },
  "status" : "aeiou",
  "name" : "doggie",
  "photoUrls" : [ "aeiou" ]
} ], contentType=application/json}, {example=<Pet>
  <id>123456</id>
  <name>doggie</name>
  <photoUrls>
    <photoUrls>string</photoUrls>
  </photoUrls>
  <tags>
  </tags>
  <status>string</status>
</Pet>, contentType=application/xml}]
         
         - parameter status: (query) Status values that need to be considered for filter

         - returns: RequestBuilder<[Pet]> 
         */
        public class func findPetsByStatus(status status: [String]?) -> RequestBuilder<[Pet]> {
            let path = "/pet/findByStatus"
            let URLString = PetstoreClientAPI.basePath + path
            
            let nillableParameters: [String:AnyObject?] = [
                "status": status
            ]
            let parameters = APIHelper.rejectNil(nillableParameters)

            let requestBuilder: RequestBuilder<[Pet]>.Type = PetstoreClientAPI.requestBuilderFactory.getBuilder()

            return requestBuilder.init(method: "GET", URLString: URLString, parameters: parameters, isBody: false)
        }
    
        /**
         
         Finds Pets by tags
         
         - GET /pet/findByTags
         - Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.
         - OAuth:
           - type: oauth2
           - name: petstore_auth
         - examples: [{example=[ {
  "tags" : [ {
    "id" : 123456789,
    "name" : "aeiou"
  } ],
  "id" : 123456789,
  "category" : {
    "id" : 123456789,
    "name" : "aeiou"
  },
  "status" : "aeiou",
  "name" : "doggie",
  "photoUrls" : [ "aeiou" ]
} ], contentType=application/json}, {example=<Pet>
  <id>123456</id>
  <name>doggie</name>
  <photoUrls>
    <photoUrls>string</photoUrls>
  </photoUrls>
  <tags>
  </tags>
  <status>string</status>
</Pet>, contentType=application/xml}]
         - examples: [{example=[ {
  "tags" : [ {
    "id" : 123456789,
    "name" : "aeiou"
  } ],
  "id" : 123456789,
  "category" : {
    "id" : 123456789,
    "name" : "aeiou"
  },
  "status" : "aeiou",
  "name" : "doggie",
  "photoUrls" : [ "aeiou" ]
} ], contentType=application/json}, {example=<Pet>
  <id>123456</id>
  <name>doggie</name>
  <photoUrls>
    <photoUrls>string</photoUrls>
  </photoUrls>
  <tags>
  </tags>
  <status>string</status>
</Pet>, contentType=application/xml}]
         
         - parameter tags: (query) Tags to filter by

         - returns: RequestBuilder<[Pet]> 
         */
        public class func findPetsByTags(tags tags: [String]?) -> RequestBuilder<[Pet]> {
            let path = "/pet/findByTags"
            let URLString = PetstoreClientAPI.basePath + path
            
            let nillableParameters: [String:AnyObject?] = [
                "tags": tags
            ]
            let parameters = APIHelper.rejectNil(nillableParameters)

            let requestBuilder: RequestBuilder<[Pet]>.Type = PetstoreClientAPI.requestBuilderFactory.getBuilder()

            return requestBuilder.init(method: "GET", URLString: URLString, parameters: parameters, isBody: false)
        }
    
        /**
         
         Find pet by ID
         
         - GET /pet/{petId}
         - Returns a pet when ID < 10.  ID > 10 or nonintegers will simulate API error conditions
         - API Key:
           - type: apiKey api_key 
           - name: api_key
         - OAuth:
           - type: oauth2
           - name: petstore_auth
         - examples: [{example={
  "tags" : [ {
    "id" : 123456789,
    "name" : "aeiou"
  } ],
  "id" : 123456789,
  "category" : {
    "id" : 123456789,
    "name" : "aeiou"
  },
  "status" : "aeiou",
  "name" : "doggie",
  "photoUrls" : [ "aeiou" ]
}, contentType=application/json}, {example=<Pet>
  <id>123456</id>
  <name>doggie</name>
  <photoUrls>
    <photoUrls>string</photoUrls>
  </photoUrls>
  <tags>
  </tags>
  <status>string</status>
</Pet>, contentType=application/xml}]
         - examples: [{example={
  "tags" : [ {
    "id" : 123456789,
    "name" : "aeiou"
  } ],
  "id" : 123456789,
  "category" : {
    "id" : 123456789,
    "name" : "aeiou"
  },
  "status" : "aeiou",
  "name" : "doggie",
  "photoUrls" : [ "aeiou" ]
}, contentType=application/json}, {example=<Pet>
  <id>123456</id>
  <name>doggie</name>
  <photoUrls>
    <photoUrls>string</photoUrls>
  </photoUrls>
  <tags>
  </tags>
  <status>string</status>
</Pet>, contentType=application/xml}]
         
         - parameter petId: (path) ID of pet that needs to be fetched

         - returns: RequestBuilder<Pet> 
         */
        public class func getPetById(petId petId: Int) -> RequestBuilder<Pet> {
            var path = "/pet/{petId}"
            path = path.stringByReplacingOccurrencesOfString("{petId}", withString: "\(petId)", options: .LiteralSearch, range: nil)
            let URLString = PetstoreClientAPI.basePath + path
            
            let nillableParameters: [String:AnyObject?] = [:]
            let parameters = APIHelper.rejectNil(nillableParameters)

            let requestBuilder: RequestBuilder<Pet>.Type = PetstoreClientAPI.requestBuilderFactory.getBuilder()

            return requestBuilder.init(method: "GET", URLString: URLString, parameters: parameters, isBody: true)
        }
    
        /**
         
         Updates a pet in the store with form data
         
         - POST /pet/{petId}
         - 
         - OAuth:
           - type: oauth2
           - name: petstore_auth
         
         - parameter petId: (path) ID of pet that needs to be updated
         - parameter name: (form) Updated name of the pet
         - parameter status: (form) Updated status of the pet

         - returns: RequestBuilder<Void> 
         */
        public class func updatePetWithForm(petId petId: String, name: String?, status: String?) -> RequestBuilder<Void> {
            var path = "/pet/{petId}"
            path = path.stringByReplacingOccurrencesOfString("{petId}", withString: "\(petId)", options: .LiteralSearch, range: nil)
            let URLString = PetstoreClientAPI.basePath + path
            
            let nillableParameters: [String:AnyObject?] = [
                "name": name,
                "status": status
            ]
            let parameters = APIHelper.rejectNil(nillableParameters)

            let requestBuilder: RequestBuilder<Void>.Type = PetstoreClientAPI.requestBuilderFactory.getBuilder()

            return requestBuilder.init(method: "POST", URLString: URLString, parameters: parameters, isBody: false)
        }
    
        /**
         
         Deletes a pet
         
         - DELETE /pet/{petId}
         - 
         - OAuth:
           - type: oauth2
           - name: petstore_auth
         
         - parameter petId: (path) Pet id to delete

         - returns: RequestBuilder<Void> 
         */
        public class func deletePet(petId petId: Int) -> RequestBuilder<Void> {
            var path = "/pet/{petId}"
            path = path.stringByReplacingOccurrencesOfString("{petId}", withString: "\(petId)", options: .LiteralSearch, range: nil)
            let URLString = PetstoreClientAPI.basePath + path
            
            let nillableParameters: [String:AnyObject?] = [:]
            let parameters = APIHelper.rejectNil(nillableParameters)

            let requestBuilder: RequestBuilder<Void>.Type = PetstoreClientAPI.requestBuilderFactory.getBuilder()

            return requestBuilder.init(method: "DELETE", URLString: URLString, parameters: parameters, isBody: true)
        }
    
        /**
         
         uploads an image
         
         - POST /pet/{petId}/uploadImage
         - 
         - OAuth:
           - type: oauth2
           - name: petstore_auth
         
         - parameter petId: (path) ID of pet to update
         - parameter additionalMetadata: (form) Additional data to pass to server
         - parameter file: (form) file to upload

         - returns: RequestBuilder<Void> 
         */
        public class func uploadFile(petId petId: Int, additionalMetadata: String?, file: NSURL?) -> RequestBuilder<Void> {
            var path = "/pet/{petId}/uploadImage"
            path = path.stringByReplacingOccurrencesOfString("{petId}", withString: "\(petId)", options: .LiteralSearch, range: nil)
            let URLString = PetstoreClientAPI.basePath + path
            
            let nillableParameters: [String:AnyObject?] = [
                "additionalMetadata": additionalMetadata,
                "file": file
            ]
            let parameters = APIHelper.rejectNil(nillableParameters)

            let requestBuilder: RequestBuilder<Void>.Type = PetstoreClientAPI.requestBuilderFactory.getBuilder()

            return requestBuilder.init(method: "POST", URLString: URLString, parameters: parameters, isBody: false)
        }
    
    }
}
