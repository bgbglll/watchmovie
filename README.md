# WatchMovie

WatchMovie is a powerful tool to help cinema manager to manage cinema.

WatchMovie is platform to share just news about movie.

WatchMovie is gathering place of who truely love movie.



## So what can I do with WatchMoive?
<br/>
To Manager:

- Release news about movie.
- Devices monitoring.
- Dispatch the movie files.
- Marketing Analyze.
- Box Office Analyze.
- Managing ticket.
- Screen & Arrange.

<br/>
To Customer:



- Reserve the ticket.
- Getting the newest information about movie.
- Comment movie and news.
- Share viewpoints.
- Find movie fancier who like themselves.

## How do I start it?

#####Setting-up
The datasource is used MySQL 5.7. 

So at first you need set the databaes config file in %PROJECT_ROOT%/src/main/resources/application.properties, the exmaple:

```xml
spring.datasource.url=jdbc:mysql://localhost:3306/watchmovie
spring.datasource.username=root
spring.datasource.password=root
```


To initialize it,
    
Just run the InitDatabaseTests or deploy it in your server (e.g. tomcat) directory.



## How do I use it?

#####1.Sign in & Sign up
There will be a ticket for who sigh up account, so no ticket no sign in.

#####2.Dispatch
Firstly, add the target server adress.

Then, select the files and drop them in upload box.

Finnally, push the button. When the dispatch is over, you will get notification.
####Note: run dispatch function needs to start receiver server which in netty-file-server project at first.
#####3.Screen & Arrange
Firstly, create playlist including movie, number of movie room, play date and play daily or weekly according to sales manager.

Secondly push the button to confirm.

In every movie play start or end, the manager will receive the notification message from system, so if there are something wrong, they will know it in first time.

#####4.News Release
Firstly enter the movie name and release date.

Secondly edit the news title, upload poster, prevue and some introduction about movie.

Finally push the button to release news.

When the news get comments, the people who release news will get notification.

...


## Build & Run without virtualization


#### Redis

##### Linux

Redis is available in the repositories of most mainstream distros. Installing it is usually a matter of running something along the lines of (for Ubuntu):

    sudo apt-get install redis

After the command finishes you can (again on Ubuntu) run:

    redis-cli ping

Redis should now reply

    PONG

##### Mac

You can install Redis with Homebrew

    brew install redis

And start it with

    redis-server
    
##### Windows
Download form https://github.com/MSOpenTech/redis/releases and install it.

