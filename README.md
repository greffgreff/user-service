<p>
  <img src="https://github.com/rently-io/user-service/actions/workflows/ci.yml/badge.svg" />
  <img src="https://github.com/rently-io/user-service/actions/workflows/cd.yml/badge.svg" />
</p>

# User Service (v2)

This Spring Boot project is one among other RESTful APIs used in the larger Rently project. More specifically, this service is intended to serve requests when users login into the Rently system for safe keeping purposes. Users are stored insinde a MySQL database using JPA. Possible requests include `GET`, `POST`, `PUT`, `DELETE`.

After each subsequent additions and changes to the codebase of the service, tests are ran and, if passed, the service is automatically deployed on to a Heroku instance [here](https://user-service-rently.herokuapp.com/) and dockerized [here](https://hub.docker.com/repository/docker/dockeroo80/rently-user-service).

Unlike the previous iteration, this endpoint no longer handles sensitive data such as passwords and salt to an authentication endpoint since autherization is now based on OAuth exclusively. As a result, users are now identified by a composite key of the `id` supplied by a provider (e.g. Google ID) and the `provider` itself (e.g. Google) in the unlikely event the id from the provider matches that of another provider. 

A middleware was added that verifies the json web tokens' validity upon every requests. Data ownership is verified by comparing JWT subject to the data's holder id. JWTs must have the [following shape](#jwt-object]) and must be encrypted using the right server secret and its corresponding hashing algorithm.

### C2 model
![C2 model](https://i.imgur.com/CqQbDQA.png)

## Objects

### Response Object

| **Field**              | **Description**                                                           |
| ---------------------- | ------------------------------------------------------------------------- |
| `timestamp`, timestamp | A timestamp of when the request was served. Format: _yyyy-MM-dd HH:mm:ss_ |
| `status` int           | The http response status code                                             |
| `content`, any         | The response data, _optional_                                             |
| `message`, string      | The response message, _optional_                                          |

### User Object

| **Field**              | **Description**                                         |
| ---------------------- | ------------------------------------------------------- |
| `id` uuid string       | The user's username                                     |
| `providerId` string    | The user's id given by the provider (e.g Google ID)     |
| `provider` string      | The user's origin (e.g. Google)                         |
| `name` string          | The user's name from the provider                       |
| `email` string         | The user's email from the provider                      |
| `createdAt`, timestamp | Timestamp of when the user was created                  |
| `updatedAt`, timestamp | Timestamp of when the last changes to the data was made |

### JWT Object

| **Field**         | **Description**              |
| ----------------- | ---------------------------- |
| `sub` uuid string | The user's id                |
| `iat` timestamp   | Issue time of the token      |
| `exp` timestamp   | Expiration time of the token |
| `jti` uuid string | The token's id               |

<br />

## Request Mappings

### `GET /api/v2/users/{id}`

Returns a json [response](#response-object) object containing one [user](#user-object) object. Permits fetching user data using a `provider` and a `provider account id`.

#### URL parameters:

|       **Parameter** | **Description**           | **Required** |
| ------------------: | ------------------------- | :----------: |
|   `provider` string | Valid provider name       |     true     |
| `providerId` string | Valid provider account id |     true     |

#### Request body parameters:

> _none_

#### Return example:

```json
{
  "timestamp": "2022-03-17 16:58:12",
  "status": 200,
  "content": {
    "id": "3bdb141f-deb6-4260-a8ac-999e6ab9c89d",
    "name": "fmullett4",
    "provider": "facebook",
    "providerId": "123123abcabc",
    "email": "fmullett4@51.la",
    "createdAt": "1617868768000",
    "updatedAt": "1635152066000"
  }
}
```

#### Possible error codes:

| **Status** | **Message**             | **Description**                                                |
| :--------: | ----------------------- | -------------------------------------------------------------- |
|   `404`    | _"Could not find user"_ | No user found on the database with specified request parameter |

<br />

### `GET /api/v2/users/{id}`

Returns a json [response](#response-object) object containing one [user](#user-object) object. Permits fetching user data by `id`.

#### URL parameters:

|    **Parameter** | **Description** | **Required** |
| ---------------: | --------------- | :----------: |
| `id` uuid string | A valid user id |     true     |

#### Request body parameters:

> _none_

#### Return example:

```json
{
  "timestamp": "2022-03-17 16:58:12",
  "status": 200,
  "content": {
    "id": "3bdb141f-deb6-4260-a8ac-999e6ab9c89d",
    "name": "fmullett4",
    "provider": "facebook",
    "providerId": "123123abcabc",
    "email": "fmullett4@51.la",
    "createdAt": "1617868768000",
    "updatedAt": "1635152066000"
  }
}
```

#### Possible error codes:

| **Status** | **Message**             | **Description**                                                |
| :--------: | ----------------------- | -------------------------------------------------------------- |
|   `404`    | _"Could not find user"_ | No user found on the database with specified request parameter |

<br />

### `POST /api/v2/`

Inserts an unregistered user in the database. Performs validation on fields and throws an error accordingly.

#### URL parameters:

> _none_

#### Request body parameters:

A [user](#user-object) object.

#### Return example:

```json
{
  "timestamp": "2022-03-17 16:58:12",
  "status": 201,
  "message": "Successfully added user to database"
}
```

#### Possible error codes:

| **Status** | **Message**                                                                                                           | **Description**                                                                                                    |
| :--------: | --------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ |
|   `409`    | _"User with provider and provider account id already"_                                                                | A user was found with a matching provider and provider account id provided in the request body, request terminated |
|   `406`    | _"A non-optional field has missing value. Value of field '`field`' was expected but got null"_                        | Non-optional field was missing                                                                                     |
|   `406`    | _"Validation failure occurred. Value of field '`field`' could not be recognized as type "`type`" (value: '`value`')"_ | Non-optional field was of the wrong type                                                                           |

<br />

### `PUT /api/v2/{id}`

Updates a user using the request body data in json format. Perform validation on fields and throws an error accordingly verifies ownership beforehand using the `subject` of the request's JWT and the URL path variable `id`.

#### URL parameters:

|    **Parameter** | **Description** | **Required** |
| ---------------: | --------------- | :----------: |
| `id` uuid string | A valid user id |     true     |

#### Request body parameters:

A [user](#user-object) object.

#### Return example:

```json
{
  "timestamp": "2022-03-17 16:58:12",
  "status": 200,
  "message": "Successfully updated user from database"
}
```

#### Possible error codes:

| **Status** | **Message**                                                                                                           | **Description**                                                                   |
| :--------: | --------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- |
|   `404`    | _"Could not find user"_                                                                                               | No user found on the database with specified request parameter                    |
|   `406`    | _"A non-optional field has missing value. Value of field '`field`' was expected but got null"_                        | Non-optional field was missing                                                    |
|   `406`    | _"Validation failure occurred. Value of field '`field`' could not be recognized as type "`type`" (value: '`value`')"_ | Non-optional field was of the wrong type                                          |
|   `401`    | _"Request is either no longer valid or has been tampered with"_                                                       | Request bearer has either expired or the subject and the data holder do not match |

<br />

### `DELETE /api/v2/{id}`

Deletes a user from the database. Performs owership verification beforehand using the `subject` of the request's JWT and the URL path variable `id`.

#### URL parameters:

|    **Parameter** | **Description** | **Required** |
| ---------------: | --------------- | :----------: |
| `id` uuid string | A valid user id |     true     |

#### Request body parameters:

> _none_

#### Return example:

```json
{
  "timestamp": "2022-03-17 16:58:12",
  "status": 200,
  "message": "Successfully deleted user from database"
}
```

#### Possible error codes:

| **Status** | **Message**                                                     | **Description**                                                                   |
| :--------: | --------------------------------------------------------------- | --------------------------------------------------------------------------------- |
|   `404`    | _"Could not find user"_                                         | No user found on the database with specified request parameter                    |
|   `401`    | _"Request is either no longer valid or has been tampered with"_ | Request bearer has either expired or the subject and the data holder do not match |
