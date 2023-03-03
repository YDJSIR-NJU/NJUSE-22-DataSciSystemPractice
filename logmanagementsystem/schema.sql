SET
FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    user_id   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(255)    NOT NULL,
    password  VARCHAR(255)    NOT NULL
);


DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
    comment_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id    INT             NOT NULL,
    comment    TEXT            NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);


DROP TABLE IF EXISTS `comment_likes`;
CREATE TABLE `comment_likes`
(
    comment_id INT NOT NULL,
    user_id    INT NOT NULL,
    PRIMARY KEY (comment_id, user_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (comment_id) REFERENCES comment (comment_id)

);

SET
FOREIGN_KEY_CHECKS=1;







