-- Down migration for removing Quartz Scheduler tables and indexes

-- Drop indexes
DROP INDEX IF EXISTS idx_qrtz_j_req_recovery;
DROP INDEX IF EXISTS idx_qrtz_j_grp;

DROP INDEX IF EXISTS idx_qrtz_t_j;
DROP INDEX IF EXISTS idx_qrtz_t_jg;
DROP INDEX IF EXISTS idx_qrtz_t_c;
DROP INDEX IF EXISTS idx_qrtz_t_g;
DROP INDEX IF EXISTS idx_qrtz_t_state;
DROP INDEX IF EXISTS idx_qrtz_t_n_state;
DROP INDEX IF EXISTS idx_qrtz_t_n_g_state;
DROP INDEX IF EXISTS idx_qrtz_t_next_fire_time;
DROP INDEX IF EXISTS idx_qrtz_t_nft_st;
DROP INDEX IF EXISTS idx_qrtz_t_nft_misfire;
DROP INDEX IF EXISTS idx_qrtz_t_nft_st_misfire;
DROP INDEX IF EXISTS idx_qrtz_t_nft_st_misfire_grp;

DROP INDEX IF EXISTS idx_qrtz_ft_trig_inst_name;
DROP INDEX IF EXISTS idx_qrtz_ft_inst_job_req_rcvry;
DROP INDEX IF EXISTS idx_qrtz_ft_j_g;
DROP INDEX IF EXISTS idx_qrtz_ft_jg;
DROP INDEX IF EXISTS idx_qrtz_ft_t_g;
DROP INDEX IF EXISTS idx_qrtz_ft_tg;

-- Drop tables
DROP TABLE IF EXISTS qrtz_fired_triggers;
DROP TABLE IF EXISTS qrtz_paused_trigger_grps;
DROP TABLE IF EXISTS qrtz_scheduler_state;
DROP TABLE IF EXISTS qrtz_locks;
DROP TABLE IF EXISTS qrtz_simple_triggers;
DROP TABLE IF EXISTS qrtz_cron_triggers;
DROP TABLE IF EXISTS qrtz_simprop_triggers;
DROP TABLE IF EXISTS qrtz_blob_triggers;
DROP TABLE IF EXISTS qrtz_triggers;
DROP TABLE IF EXISTS qrtz_job_details;
DROP TABLE IF EXISTS qrtz_calendars;