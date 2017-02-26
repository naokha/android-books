# Minimalist android app

> Small android app for learning purposes. It's some kind of master / detail app with books.

# Architecture

I chose to have only one template for both orientations. It's then programmatically updated by injecting the fragments to the good containers (frame layouts). I used res variables to automatically display the containers based on the phone orientation

# Tests
I did some UI tests only, with espresso

# Problems
* Some problems with the backstack: sometimes the list fragment was multiple times in the view hierarchy, causing UI overlapping. I had to remove all the backstack each time we go into the list view

# Improvement
* Add more tests (with presenter and robolectric)
* May be a cleaner way to manage fragments injection into the containers
