[![shield]][repo]

[shield]: https://github.com/greffgreff/user-service/workflows/User%20Endpoint%20CI%20CD/badge.svg
[repo]: https://github.com/greffgreff/user-service/

# User Service V1.0

This project is one among other RESTful APIs used in the larger Rently.io project whose frontend can be found [here](https://github.com/greffgreff/rently). More specifically, this endpoint is intended to serve requests regarding users-related data. Possible requests include `GET`, `POST`, `PUT`, `DELETE`.

**DEPRECATED** Due to security concerns, it was decided to drop the use of this version of the endpoint so it is now deprecated. Reasons for this can be found [at the bottom](#deprecated).

<br />

## Response Data

### Response Object

|**Field**|**Description**|
|---|---|
| `timestamp`, timestamp | A timestamp of when the request was served. Format: *yyyy-MM-dd HH:mm:ss* |
| `status` int | The http response status code |
| `content`, any | The response data, *optional* |
| `message`, string | The response message, *optional* |

### User Object

|**Field**|**Description**|
|---|---|
| `id` guid | The user's unique surrogate key |
| `username` string | The user's username |
| `fullName` string | The user's first and last name |
| `email` string | The user's email address, unique |
| `gender` string | The user's gender |
| `phone` string | The user's phone number |
| `password`, string | A hashed password |
| `salt`, string | The password's corresponding salt |
| `createdOn`, timestamp | Timestamp of when the user was created  |
| `updatedOn`, timestamp | Timestamp of when the last changes to the data was made |

<br />

## Request Mappings

### `GET /api/v1/users/id/{id}`

Returns a json [response](#response-object) object containing one [user](#user-object) object.

#### URL parameters: 
| **Parameter** | **Description** | **Required**
|---:|---|:---:|
| `id` string | A valid user id | true |

#### Request body parameters: 
> *none*

#### Return example:
```json
{
    "timestamp": "2022-03-17 16:58:12",
    "status": 200,
    "content": {
        "id": "3bdb141f-deb6-4260-a8ac-999e6ab9c89d",
        "username": "fmullett4",
        "fullName": "Florentia Mullett",
        "email": "fmullett4@51.la",
        "gender": "Male",
        "phone": "+86 (177) 173-2250",
        "password": "5JbyPYKH9B",
        "salt": "742buf8694",
        "createdOn": "1617868768000",
        "updatedOn": "1635152066000"
    }
}
```

#### Possible error codes:
| **Status** | **Message** | **Description** | 
|:---:|---|---|
| `404` | *"Could not find user with specified arguments"* | No user found on the database with specified request parameter | 


### `GET /api/v1/users/username/{username}`

Returns a json [response](#response-object) object containing an array of [user](#user-object) objects.

#### URL parameters: 
| **Parameter** | **Description** | **Required**
|---:|---|:---:|
| `username` string | A valid username | true |

#### Request body parameters: 
> *none*

#### Return example:
```json
{
    "timestamp": "2022-03-17 16:58:12",
    "status": 200,
    "content": [
      {
          "id": "3bdb141f-deb6-4260-a8ac-999e6ab9c89d",
          "username": "fmullett4",
          "fullName": "Florentia Mullett",
          "email": "fmullett4@51.la",
          "gender": "Male",
          "phone": "+86 (177) 173-2250",
          "password": "5JbyPYKH9B",
          "salt": "742buf8694",
          "createdOn": "1617868768000",
          "updatedOn": "1635152066000"
      }
    ]
}
```

#### Possible error codes:
| **Status** | **Message** | **Description** | 
|:---:|---|---|
| `404` | *"Could not find user with specified arguments"* | No user found on the database with specified request parameter | 


### `GET /api/v1/users/email/{email}`

Returns a json [response](#response-object) object containing an array of [user](#user-object) objects.

#### URL parameters: 
| **Parameter** | **Description** | **Required**
|---:|---|:---:|
| `email` string | A valid email address | true |

#### Request body parameters: 
> *none*

#### Return example:
```json
{
    "timestamp": "2022-03-17 16:58:12",
    "status": 200,
    "content": [
      {
          "id": "3bdb141f-deb6-4260-a8ac-999e6ab9c89d",
          "username": "fmullett4",
          "fullName": "Florentia Mullett",
          "email": "fmullett4@51.la",
          "gender": "Male",
          "phone": "+86 (177) 173-2250",
          "password": "5JbyPYKH9B",
          "salt": "742buf8694",
          "createdOn": "1617868768000",
          "updatedOn": "1635152066000"
      }
    ]
}
```

#### Possible error codes:
| **Status** | **Message** | **Description** | 
|:---:|---|---|
| `404` | *"Could not find user with specified arguments"* | No user found on the database with specified request parameter | 


### `POST /api/v1/users`

Creates a user using the request body data in json format and inserts it to the database. 

#### URL parameters: 
> *none*

#### Request body parameters: 
| **Parameter** | **Description** | **Required**
|---:|---|:---:|
| `username` string | A unique username | true |
| `email` string | A unique email address | true |
| `password` string | A none empty password | true |

#### Return example:
```json
{
    "timestamp": "2022-03-17 16:58:12",
    "status": 201,
    "message": "Successfully added user to database"
}
```

#### Possible error codes:
| **Status** | **Message** | **Description** | 
|:---:|---|---|
| `400` | *"Username unset"* | No username was provided in the request body, request terminated | 
| `400` | *"Email address unset"* | No email was provided in the request body, request terminated | 
| `400` | *"Password unset"* | No password was provided in the request body, request terminated | 
| `409` | *"User with matching email already exists"* | A user was found with a matching email address provided in the request body, request terminated | 
| `409` | *"User with matching username already exists"* | A user was found with a matching username provided in the request body, request terminated | 
| `406` | *"No content found in request body"* | No content was provided in the request body | 
| `406` | *"Invalid data provided by client"* | The content in the request body was malformated or is not in json format | 


### `PUT /api/v1/users/{id}`

Updates a user using the request body data in json format from the database. 

#### URL parameters: 
| **Parameter** | **Description** | **Required**
|---:|---|:---:|
| `id` string | A valid user id | true |

#### Request body parameters: 
| **Parameter** | **Description** | **Required**
|---:|---|:---:|
| `username` string | A unique username | false |
| `fullName` string | Full name of the user | false |
| `email` string | A unique email address | false |
| `gender` string | Gender of the user | false |
| `phone` string | Phone number of the user | false |
| `password` string | A new password | false |
| `salt` string | A new password salt | false |

#### Return example:
```json
{
    "timestamp": "2022-03-17 16:58:12",
    "status": 200,
    "message": "Successfully updated user in database"
}
```

#### Possible error codes:
| **Status** | **Message** | **Description** | 
|:---:|---|---|
| `404` | *"Could not find user with specified arguments"* | No user found on the database with specified request parameter | 
| `409` | *"User with matching email already exists"* | A user was found with a matching email address provided in the request body, request terminated | 
| `409` | *"User with matching username already exists"* | A user was found with a matching username provided in the request body, request terminated | 
| `406` | *"No content found in request body"* | No content was provided in the request body | 
| `406` | *"Invalid data provided by client"* | The content in the request body was malformated or is not in json format | 


### `DELETE /api/v1/users/{id}`

Deletes one user from the database using the . 

#### URL parameters: 
| **Parameter** | **Description** | **Required**
|---:|---|:---:|
| `id` string | A valid user id | true |

#### Request body parameters: 
> *none*

#### Return example:
```json
{
    "timestamp": "2022-03-17 16:58:12",
    "status": 200,
    "message": "Successfully deleted user from database"
}
```

#### Possible error codes:
| **Status** | **Message** | **Description** | 
|:---:|---|---|
| `404` | *"Could not find user with specified arguments"* | No user found on the database with specified request parameter | 
