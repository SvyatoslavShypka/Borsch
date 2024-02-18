CREATE TABLE IF NOT EXISTS access.friendgroups (
                                    id UUID,
                                    title VARCHAR(255) NOT NULL,
                                    code VARCHAR(36) NOT NULL,
                                    CONSTRAINT friendgroups_pkey PRIMARY KEY (id)
);

ALTER TABLE access.users
       ADD FOREIGN KEY (friendgroup_id) REFERENCES access.friendgroups(id);