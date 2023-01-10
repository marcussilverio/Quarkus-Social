CREATE DATABASE quarkus-social;
USE quarkus-social;
CREATE TABLE IF NOT EXISTS public.users
(
    id bigserial NOT NULL PRIMARY KEY,
    name varchar(100) NOT NULL,
    age integer NOT NULL
);
CREATE TABLE IF NOT EXISTS public.posts
(
    id bigserial NOT NULL PRIMARY KEY,
    p_text varchar(400) NOT NULL,
    dateTime timestamp NOT NULL,
    user_id bigint NOT NULL REFERENCES public.users(id)
);

CREATE TABLE IF NOT EXISTS public.followers
(
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint NOT NULL REFERENCES public.users(id),
    follower_id bigint NOT NULL REFERENCES public.user(id)
);