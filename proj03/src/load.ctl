LOAD DATA
INFILE './2010.csv'
INTO TABLE AIMS2010
fields terminated by "," optionally enclosed by '"'		  
(
	Fiscal_Year,
	State,
	County,
	LEA_EntityID,
	LEA_CTDSnum,
	LEA_Name,
	School_EntityID,
	School_CTDSnum,
	School_Name,
	S_charter,
	Math_mean,
	Math_PercFallsFarBelow,
	Math_PercApproaches,
	Math_PercMeets,
	Math_PercExceeds,
	Math_PercPassing,
	Reading_mean,
	Reading_PercFallsFarBelow,
	Reading_PercApproaches,
	Reading_PercMeets,
	Reading_PercExceeds,
	Reading_PercPassing,
	Writing_mean,
	Writing_PercFallsFarBelow,
	Writing_PercApproaches,
	Writing_PercMeets,
	Writing_PercExceeds,
	Writing_PercPassing,
	Science_mean,
	Science_PercFallsFarBelow,
	Science_PercApproaches,
	Science_PercMeets,
	Science_PercExceeds,
	Science_PercPassing
)