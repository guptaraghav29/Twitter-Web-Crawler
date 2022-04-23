import tweepy
from dotenv import dotenv_values

# Load .env file
config = dotenv_values(".env")

# Authenticate into the twitter api
auth = tweepy.OAuth2BearerHandler(config["BEARER_TOKEN"])
api = tweepy.API(auth)

# Twitter API now available for use.



