CREATE TABLE certificate
(
	serialnumber bytea NOT NULL,
	content bytea NOT NULL,
	CONSTRAINT PRIMARY KEY (serialnumber)
);

CREATE TABLE webspace
(
	identifier character varying(600) NOT NULL,
	certificate bytea NOT NULL,
	keypassphrase bytea,
	key bytea,
	CONSTRAINT PRIMARY KEY (identifier),
	CONSTRAINT FOREIGN KEY (certificate)
	REFERENCES certificate (serialnumber)
	ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE message
(
	receiver character varying(600) NOT NULL,
	"timestamp" timestamp with time zone NOT NULL DEFAULT statement_timestamp(),
	content bytea NOT NULL,
	idmessage bigserial NOT NULL,
	retrieved boolean NOT NULL DEFAULT false,
	CONSTRAINT PRIMARY KEY (idmessage),
	CONSTRAINT FOREIGN KEY (receiver)
		REFERENCES webspace (identifier)
		ON UPDATE CASCADE ON DELETE CASCADE
);