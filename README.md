# SiteUpdateAlerter
Simple code to monitor a site and receive a notification when it changes.

I used this code the first time to monitor a Covid-19 local vaccination site in order to be notified as soon as the site would have been changed (that meant I could reserve a vaccine dose).

In order to use the code, run the main method of Alerter class (or build the app) with only one parameter on CLI: the path of a configuration json file.
This file must contain a `url` key (the site to monitor) and a `checkIntervalSec` (integer value, that is the checking frequency).
An example of the json file is available.
