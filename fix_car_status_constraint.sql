-- виправлення constraint на статус автомобіля

ALTER TABLE cars DROP CONSTRAINT IF EXISTS cars_status_check;

ALTER TABLE cars ADD CONSTRAINT cars_status_check
    CHECK (status IN ('AVAILABLE', 'RESERVED_PENDING', 'RESERVED', 'SOLD', 'RESERVED_FOR_TEST_DRIVE'));

SELECT conname, pg_get_constraintdef(oid)
FROM pg_constraint
WHERE conrelid = 'cars'::regclass;

