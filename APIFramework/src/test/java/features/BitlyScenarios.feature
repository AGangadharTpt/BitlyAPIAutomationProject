Feature: Validating Bitly APIs
@Bitlinks @Regression
Scenario Outline: Verify a Bitlink is being created
Given Update payload for "CreateBitlinks" with "long_url" as "<LongUrl>", "domain" as "<Domain>" and "title" as "<Title>"
When user calls the API "CreateBitlinkAPI"
Then the API call got executed with status code 200
And verify "long_url" in response body is "<LongUrl>"
And verify "title" in response body is "<Title>"
And fetch Bitlink and BitlinkID from the response

Examples:
	|LongUrl 	             | Domain     |Title		              |
	|https://dev.bitly.com | bit.ly     |Bitly API Documentation|

    
@Bitlinks @Regression    
Scenario: Verify creating a Bitlink with payment restrictions
Given get the payload for "CreateBitlinksPaymentRestrictions"
When user calls the API "CreateBitlinkAPI"
Then the API call got executed with status code 402
And verify "message" in response body is "UPGRADE_REQUIRED"
And verify "resource" in response body is "deeplinks"
And verify "description" in response body is "You must upgrade your account to access this resource."   

@Bitlinks @Regression
Scenario: Verify creating a Bitlink with bad request
Given get the payload for "CreateBitlinksWithBadRequest"
When user calls the API "CreateBitlinkAPI"
Then the API call got executed with status code 400
And verify "message" in response body is "INVALID_ARG_LONG_URL"
And verify "resource" in response body is "bitlinks"
And verify "description" in response body is "The value provided is invalid."        

@Bitlinks @Regression    
Scenario: Verify a GetBitlinkAPI created
Given get and update Url details for "GetBitlinks"
When user calls the API "GetBitlinksAPI"
Then the API call got executed with status code 200
And verify "link" in response body


@Groups @Regression    
Scenario: Verify a GetGroupsAPI
Given get and update Url details for "GetGroups"
When user calls the API "GetGroupsAPI"
Then the API call got executed with status code 200
And verify "name" in response body is "agangadhar007"
And verify "guid" in response body is "Bm5bfiIUP6g"

@Groups @Regression    
Scenario: Verify a GetGroupsAPI with invalid GroupID
Given get and update Url details for "GetGroupsInvalidGroupID"
When user calls the API "GetGroupsInvalidGroupIDAPI"
Then the API call got executed with status code 403
And verify "message" in response body is "FORBIDDEN"
And verify "resource" in response body is "groups"
And verify "description" in response body is "You are currently forbidden to access this resource."

@Groups @Regression    
Scenario: Verify a GetGroupsAPI with invalid url
Given get and update Url details for "GetGroupsInvalidUrl"
When user calls the API "GetGroupsInvalidUrlAPI"
Then the API call got executed with status code 404
