Dumps selected public data from the GitHub API to the specified PostgreSQL database.

Looks for the DATABASE_URL environment variable to define the database location.

Run the following to build

`$ mvn package`

Currently includes a snapshot of egit-github as a local maven repository, maven
should be able to find the rest of the dependencies.

Includes half-finished Event support that's not used.

TODO:
commit+commit comments for repos
logging
403/404 handling

Current flow on heroku:

`heroku open`

Starts the dump process to db provided by DATABASE_URL.

`heroku pgbackups:capture --expire`

When the API dump is finished, dump the db.