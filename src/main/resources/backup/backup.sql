--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO postgres;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO postgres;

--
-- Name: t_file; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_file (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    size bigint,
    parent_id bigint
);


ALTER TABLE public.t_file OWNER TO postgres;

--
-- Name: t_file_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.t_file_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.t_file_seq OWNER TO postgres;

--
-- Name: t_folder; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_folder (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    parent_id bigint
);


ALTER TABLE public.t_folder OWNER TO postgres;

--
-- Name: t_folder_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.t_folder_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.t_folder_seq OWNER TO postgres;

--
-- Name: t_role_file; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_role_file (
    role_object_id bigint NOT NULL,
    file_id bigint NOT NULL
);


ALTER TABLE public.t_role_file OWNER TO postgres;

--
-- Name: t_role_folder; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_role_folder (
    role_object_id bigint NOT NULL,
    folder_id bigint NOT NULL
);


ALTER TABLE public.t_role_folder OWNER TO postgres;

--
-- Name: t_role_on_object; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_role_on_object (
    id bigint NOT NULL,
    role_name character varying(255) NOT NULL,
    CONSTRAINT t_role_on_object_role_name_check CHECK (((role_name)::text = ANY ((ARRAY['READER'::character varying, 'AUTHOR'::character varying])::text[])))
);


ALTER TABLE public.t_role_on_object OWNER TO postgres;

--
-- Name: t_role_on_object_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.t_role_on_object_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.t_role_on_object_seq OWNER TO postgres;

--
-- Name: t_role_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_role_user (
    user_id bigint NOT NULL,
    role_object_id bigint NOT NULL
);


ALTER TABLE public.t_role_user OWNER TO postgres;

--
-- Name: t_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_user (
    id bigint NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    role character varying(255) NOT NULL
);


ALTER TABLE public.t_user OWNER TO postgres;

--
-- Name: t_user_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.t_user_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.t_user_seq OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_id_seq OWNER TO postgres;

--
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) FROM stdin;
01-create-t_user	Ксения Голубкова	liquibase/v_1.0/01-create-t_user.xml	2024-11-20 20:53:17.452044	1	EXECUTED	9:d42d3422208d19c7ac2e101951dbfdfa	createTable tableName=t_user; createIndex indexName=idx_username_email, tableName=t_user; createSequence sequenceName=user_id_seq		\N	4.27.0	\N	\N	2125197421
02-create-t_folder	Ксения Голубкова	liquibase/v_1.0/02-create-t_folder.xml	2024-11-24 14:56:34.144443	2	EXECUTED	9:a4ae29200d22c34eebc391ad986e9287	createTable tableName=t_folder; addForeignKeyConstraint baseTableName=t_folder, constraintName=fk_t_folder_parent, referencedTableName=t_folder		\N	4.27.0	\N	\N	2449394122
03-create-t_file	Ксения Голубкова	liquibase/v_1.0/03-create-t_file.xml	2024-11-24 14:56:34.154301	3	EXECUTED	9:d4c051fc7aa7140461760bc9e5dd8636	createTable tableName=t_file; addForeignKeyConstraint baseTableName=t_file, constraintName=fk_t_file_parent, referencedTableName=t_folder		\N	4.27.0	\N	\N	2449394122
04-create-t_role_on_object	Ксения Голубкова	liquibase/v_1.0/04-create-t_role_on_object.xml	2024-11-26 14:58:45.256119	4	EXECUTED	9:d41d8cd98f00b204e9800998ecf8427e	empty		\N	4.27.0	\N	\N	2622325246
\.


--
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
1	f	\N	\N
\.


--
-- Data for Name: t_file; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.t_file (id, name, size, parent_id) FROM stdin;
402	cloud.png	16341	2
403	main.py	230	1
404	MainWindow.py	6528	102
452	queen.png	3197	1
453	queen.png	3197	103
502	1.docx	21433	\N
503	2.docx	676157	\N
504	1.docx	21433	102
505	5.csv	14792	\N
506	709NxhKr2pE.jpg	115658	1
552	1.docx	21433	\N
602	1.docx	21433	52
652	ParseJSON.lgp	23157	\N
653	Демопример Парсинг JSON.url	123	\N
\.


--
-- Data for Name: t_folder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.t_folder (id, name, parent_id) FROM stdin;
1	test	\N
2	good	1
52	test	\N
102	test	2
103	test1	2
152	creating	\N
153	testing	\N
154	testing1212	\N
202	1111	\N
\.


--
-- Data for Name: t_role_file; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.t_role_file (role_object_id, file_id) FROM stdin;
352	402
353	403
354	404
402	452
403	453
452	502
453	503
454	504
455	505
456	506
502	552
602	505
603	602
652	652
653	653
702	653
\.


--
-- Data for Name: t_role_folder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.t_role_folder (role_object_id, folder_id) FROM stdin;
203	2
252	52
302	102
303	103
304	1
552	152
553	153
554	154
604	202
\.


--
-- Data for Name: t_role_on_object; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.t_role_on_object (id, role_name) FROM stdin;
102	AUTHOR
103	AUTHOR
104	AUTHOR
105	AUTHOR
106	AUTHOR
152	READER
202	READER
203	AUTHOR
252	AUTHOR
302	AUTHOR
303	AUTHOR
304	AUTHOR
352	AUTHOR
353	AUTHOR
354	AUTHOR
402	AUTHOR
403	AUTHOR
452	AUTHOR
453	AUTHOR
454	AUTHOR
455	AUTHOR
456	AUTHOR
502	AUTHOR
552	AUTHOR
553	AUTHOR
554	AUTHOR
602	READER
603	AUTHOR
604	AUTHOR
652	AUTHOR
653	AUTHOR
702	READER
\.


--
-- Data for Name: t_role_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.t_role_user (user_id, role_object_id) FROM stdin;
1	102
1	103
1	104
1	105
1	106
19	202
1	203
1	252
1	302
1	303
1	304
1	352
1	353
1	354
1	402
1	403
1	452
1	453
1	454
1	455
1	456
19	502
1	552
1	553
1	554
19	602
1	603
1	604
19	652
1	653
19	702
\.


--
-- Data for Name: t_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.t_user (id, username, password, email, role) FROM stdin;
2	фыфвфыв	$2a$10$S5ApxKox1m9oYJfOPel/JekYQPq6cFcmkJZbeNeZY.wqVNCdj4f02	test@test.com	ROLE_USER
3	admin11	$2a$10$6tW0OFKNIlUNlZR1KsVY3.3by0HTsEOEB8MUpi7oAWn1vchk.IVLC	kryt.master@yandex.ru	ROLE_USER
4	ывывыввыы	$2a$10$PJBjTf/w.1QC4VKEQPn4cOWhIVOwYAKealaZo79SH.7W./N4OvsRW	kryt.master@yandex.ruq	ROLE_USER
8	username@name.ru	$2a$10$RGjQzP9/F/nd.74Ps.GWs.M4KAtvHy63o9xE8qywE8/SZeNZre2s.	username@name.ru	ROLE_USER
1	admin	$2a$10$wf5CFdAv5h13erObtMi4Hu9k8RGSVKnY.xIiQLWF8MK5/i/GWNTCe	test@test.ru	ROLE_ADMIN
9	testt	$2a$10$sZMuDRvc8hbDBB2AaPoTL.lgaliqqFKiujEFlX3zU0W6PQ81fMFQa	t@t.tt	ROLE_USER
10	asdas	$2a$10$yQxxv/6zx.ziaw1SpPNit.ZdjOcExWOAdeZ5MhHdZtam0/qrgbGku	asas@gf.ru	ROLE_USER
11	fdfdf	$2a$10$CBVD7EJoBqVa0gwUzmEDoOmid.h0iSnc/cE0eI1JN8nkiSx0U3i1e	fgfgfg@gffg.ru	ROLE_USER
12	aaaa	$2a$10$hiRhicQTyJWJMdettP78deo0aiYxQu7C1XoYhIsqHfYs8Vbgqc7S2	aaaa@aaaa.ru	ROLE_USER
13	sss	$2a$10$RISgxwNw12isRKXox2iGb.8kzqkaAD6lk1db6oNgP2aFBf3cHjUm2	sss@sss.ru	ROLE_USER
14	user	$2a$10$0gNdZTq.puZ3ts/qNY52i.B3vsT7M4/eOXD4.d1TU6Bw22oaXQpCW	user@us.ru	ROLE_USER
15	фффф	$2a$10$qOnl4czWIZywUBwjUHgyqOgS523W7.MS1SYYN6gypIzZ6uVS.V8CC	aaaa@aaaa.rua	ROLE_USER
16	aaa	$2a$10$/Cf3f0Ma/tNylEMbTOG3Su.yI13/1Hhv/p6FoZ366KkvxqnVsjGpO	aaa@aaas.ryu	ROLE_USER
19	uuu@uu.uu	$2a$10$DZ2WVMC22aAQDWbbCUfgzeTGL4Gslqw1fHHYtzZMWHX9XJ/aXAo4e	uuu@uu.uu	ROLE_USER
20	ывыв	$2a$10$6oQS4tQWKsAuEUtT..ePCubr5rPby3zgOYecY49IsmIZJ3XlwceQC	kryt.master@yandex.ruaa	ROLE_USER
21	ывывasasas	$2a$10$ncHzuWC.JcoYZxXzlU2KQOMoOhNIGfc2bUfHFbZOphqsEmE15pVWq	asas@gf.ruaa	ROLE_USER
22	asaasasas	$2a$10$EO7nDzY8sfPza0DI5Jt3cupWO6EyOOAww.9CuFopKOz4I7d7NYVs6	asasasasas@asas.ru	ROLE_USER
23	asaasasasa	$2a$10$EFMVSCT3awsuUS1pWzHDMeeIN55E2nYrAmk.wjHOW9wYm1cRJi63S	asasasasaas@asas.ru	ROLE_USER
\.


--
-- Name: t_file_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.t_file_seq', 701, true);


--
-- Name: t_folder_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.t_folder_seq', 251, true);


--
-- Name: t_role_on_object_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.t_role_on_object_seq', 751, true);


--
-- Name: t_user_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.t_user_seq', 1, false);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 23, true);


--
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- Name: t_file t_file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_file
    ADD CONSTRAINT t_file_pkey PRIMARY KEY (id);


--
-- Name: t_folder t_folder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_folder
    ADD CONSTRAINT t_folder_pkey PRIMARY KEY (id);


--
-- Name: t_role_file t_role_file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_file
    ADD CONSTRAINT t_role_file_pkey PRIMARY KEY (role_object_id, file_id);


--
-- Name: t_role_folder t_role_folder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_folder
    ADD CONSTRAINT t_role_folder_pkey PRIMARY KEY (role_object_id, folder_id);


--
-- Name: t_role_on_object t_role_on_object_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_on_object
    ADD CONSTRAINT t_role_on_object_pkey PRIMARY KEY (id);


--
-- Name: t_role_user t_role_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_user
    ADD CONSTRAINT t_role_user_pkey PRIMARY KEY (user_id, role_object_id);


--
-- Name: t_user t_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user
    ADD CONSTRAINT t_user_pkey PRIMARY KEY (id);


--
-- Name: idx_username_email; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_username_email ON public.t_user USING btree (username, email);


--
-- Name: t_role_folder fk4cx4q89ccxjgdftj8dvey984f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_folder
    ADD CONSTRAINT fk4cx4q89ccxjgdftj8dvey984f FOREIGN KEY (folder_id) REFERENCES public.t_folder(id);


--
-- Name: t_role_user fk6omid3yd57ybxksh8ygy9fj4e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_user
    ADD CONSTRAINT fk6omid3yd57ybxksh8ygy9fj4e FOREIGN KEY (role_object_id) REFERENCES public.t_role_on_object(id);


--
-- Name: t_role_user fk7vpgdv8n0p61tspkgg833afhi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_user
    ADD CONSTRAINT fk7vpgdv8n0p61tspkgg833afhi FOREIGN KEY (user_id) REFERENCES public.t_user(id);


--
-- Name: t_file fk_t_file_parent; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_file
    ADD CONSTRAINT fk_t_file_parent FOREIGN KEY (parent_id) REFERENCES public.t_folder(id);


--
-- Name: t_folder fk_t_folder_parent; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_folder
    ADD CONSTRAINT fk_t_folder_parent FOREIGN KEY (parent_id) REFERENCES public.t_folder(id);


--
-- Name: t_role_file fkb5eiikunaoe4qq6qinge4o7lx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_file
    ADD CONSTRAINT fkb5eiikunaoe4qq6qinge4o7lx FOREIGN KEY (role_object_id) REFERENCES public.t_role_on_object(id);


--
-- Name: t_role_file fkdkn6vl68yk00tqudof6whxpcf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_file
    ADD CONSTRAINT fkdkn6vl68yk00tqudof6whxpcf FOREIGN KEY (file_id) REFERENCES public.t_file(id);


--
-- Name: t_role_folder fkovp9u91nqctqkmraj3imqhngp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role_folder
    ADD CONSTRAINT fkovp9u91nqctqkmraj3imqhngp FOREIGN KEY (role_object_id) REFERENCES public.t_role_on_object(id);


--
-- PostgreSQL database dump complete
--

