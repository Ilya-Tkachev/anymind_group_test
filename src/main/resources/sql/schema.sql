CREATE TABLE IF NOT EXISTS incoming
(
    received timestamp with time zone NOT NULL,
    amount numeric NOT NULL
);

CREATE TABLE IF NOT EXISTS statistic
(
    status_at timestamp with time zone NOT NULL,
    amount numeric NOT NULL
)