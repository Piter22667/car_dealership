-- виправлення constraint на статус тест-драйву в таблиці test_drives

-- спочатку перевіряємо які статуси є в базі
SELECT DISTINCT current_status FROM test_drives;

-- оновлюємо існуючі дані якщо потрібно (зміна CANCELLED на CANCELED)
UPDATE test_drives
SET current_status = 'CANCELED'
WHERE current_status = 'CANCELLED';

-- видалення старого constraint
ALTER TABLE test_drives DROP CONSTRAINT IF EXISTS test_drives_current_status_check;

-- додавання нового constraint з усіма статусами з enum TestDriveStatus
ALTER TABLE test_drives ADD CONSTRAINT test_drives_current_status_check
    CHECK (current_status IN ('SCHEDULED', 'APPROVED', 'COMPLETED', 'CANCELED'));

-- перевірка constraint
SELECT conname, pg_get_constraintdef(oid)
FROM pg_constraint
WHERE conrelid = 'test_drives'::regclass;

