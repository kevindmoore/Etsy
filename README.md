# Etsy App

### Design Decisions
1. Only load needed JSON data. No need to load all data
2. Using older version of Glide. Much easier to use
3. Use RxJava for asynchronous downloading of data
4. Use Retrofit for actual downloading and converting between JSON and models
5. Use RecyclerView for list of items
6. Sooo much more I could do but needed to stop

### Issues
1. Infinite scrolling was implemented in a very ineficient way. Currently it keeps adding data to the list. Memory constraints may cause that to be a problem.
2. Idealy I would have kept a buffer of items in memory and would keep a page ahead of the list and a page behind.
