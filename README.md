Notes From Author:
This is my first real scala app, let alone using the Play framework. There is a lot work that needs to be done, and polish
to be added. That said, it is fully functional. Enjoy

# CK lunchRoulette
CK Lunch Roulette - A scala app that will match up random pairs of people for lunch.

# To Install
Make sure PostgreSQL has database lunch with appriopriate user marked in config. (You may change user if you want)
Run like any other Scala Play app. Evolution will spin up SCHEMA's and tables.
Thats it!


# TODO
Current app doesn't support an odd number of users in the lunch Roulette. Haven't decieded what to do with the
third person.

Some heavy clean up and better FE would be nice.

Pairs need their own models. Currently they are in the user model.

Pairs should recieve a notification e-mail that they have been paired with someone.

Make Pairs page 100% responsive. Its the only page that isn't.

Make countdown clock.

Add timezone support