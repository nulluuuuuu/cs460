select salary from mccann.employee where ssn in(select essn from mccann.works_on minus select essn from mccann.works_on where pno in (select pnumber from mccann.project where pname='ProductY')) and dno=5;
