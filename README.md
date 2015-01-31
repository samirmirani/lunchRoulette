Notes From Author:
This is my first real scala app, let alone using the Play framework. There is a lot work that needs to be done, and polish
to be added. That said, it is fully functional. Enjoy

# CK lunchRoulette
CK Lunch Roulette - A scala app that will match up random pairs of people for lunch.

# To Install
Make sure PostgreSQL has database lunch with appriopriate user marked in config. (You may change user if you want)
Run like any other Scala Play app. Evolution will spin up SCHEMA's and tables.
Get Standard Play Framework config file and add:

db.default.driver=org.postgresql.Driver

db.default.url="jdbc:postgresql://localhost/lunch"

db.default.user="SomeString"

db.default.password="SomeString"

mailservice.enabled=false

smtp.debug=false

smtp.host=

smtp.port=587

smtp.isSsl=yes

smtp.tls=yes

smtp.password=

smtp.user=

application.name ="SomeString"


# TODO

Make countdown clock.

Add timezone support

# Shoutouts:
http://html5up.net/ - For the awesome template overflow.