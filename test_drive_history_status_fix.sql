-- оновлення існуючих даних
UPDATE test_drive_status_history
SET status = 'CANCELED'
WHERE status = 'CANCELLED';

-- видалення старого constraint
ALTER TABLE test_drive_status_history DROP CONSTRAINT IF EXISTS test_drive_status_history_status_check;

-- додавання нового constraint
ALTER TABLE test_drive_status_history ADD CONSTRAINT test_drive_status_history_status_check
    CHECK (status IN ('SCHEDULED', 'APPROVED', 'COMPLETED', 'CANCELED'));

-- перевірка constraint
SELECT conname, pg_get_constraintdef(oid)
FROM pg_constraint
WHERE conrelid = 'test_drive_status_history'::regclass;


-- оновлення існуючих даних в test_drives
UPDATE test_drives
SET current_status = 'CANCELED'
WHERE current_status = 'CANCELLED';

-- видалення старого constraint
ALTER TABLE test_drives DROP CONSTRAINT IF EXISTS test_drives_current_status_check;

-- додавання нового constraint
ALTER TABLE test_drives ADD CONSTRAINT test_drives_current_status_check
    CHECK (current_status IN ('SCHEDULED', 'APPROVED', 'COMPLETED', 'CANCELED'));

-- перевірка constraint
SELECT conname, pg_get_constraintdef(oid)
FROM pg_constraint
WHERE conrelid = 'test_drives'::regclass;
