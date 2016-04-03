CREATE TABLE actionlog
(
  id SERIAL PRIMARY KEY NOT NULL,
  person_uuid VARCHAR(255),
  user_uuid VARCHAR(255),
  entry VARCHAR(255),
  action_date TIMESTAMPTZ
);

CREATE TABLE identifiers
(
  id SERIAL PRIMARY KEY NOT NULL,
  uuid VARCHAR(255),
  person_uuid VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL,
  value VARCHAR(255) NOT NULL,
  label VARCHAR(255),
  url VARCHAR(255),
  state_changed_date DATE,
  state_changed_by VARCHAR(255),
  state VARCHAR(255),
  orcid_access_token VARCHAR(255),
  orcid_auth_code VARCHAR(255)
);

CREATE TABLE people
(
  id SERIAL PRIMARY KEY NOT NULL,
  uuid VARCHAR(255),
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255),
  full_name VARCHAR(255),
  job_title VARCHAR(255),
  school VARCHAR(255),
  department VARCHAR(255),
  picture_url VARCHAR(255),
  orcid_status VARCHAR(255)
);

CREATE UNIQUE INDEX people_email_key ON people (email);
