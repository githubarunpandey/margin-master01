package com.margin.repository;



import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.margin.entity.FnoFile;



public interface FnoFileRepository extends JpaRepository<FnoFile, Long> {

	@Query(value="SELECT adjustment_value FROM ant_upload_maildata_fno WHERE scrip=:scrip and type=:type and date(created_at) = :date ", nativeQuery = true)
	String getAdjustmentValue(@Param("type") String type, @Param("scrip") String scrip, @Param("date") LocalDate createdAt);
	
	
	@Query(value="SELECT * FROM ant_upload_maildata_fno WHERE scrip=:scrip and type=:type and created_at = :date ", nativeQuery = true)
	FnoFile getExitingData(@Param("type") String type, @Param("scrip") String scrip, @Param("date") LocalDate createdAt);
	
}

