package com.margin.Serivce.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.margin.Serivce.FnoFileService;
import com.margin.entity.FnoFile;
import com.margin.repository.FnoFileRepository;

@Service
public class FnoFileServiceImpl implements FnoFileService {

	@Autowired
	FnoFileRepository fnoFileRepository;

	@Override
	public FnoFile processFno(FnoFile fnoFile) {

		double adjustmentValue = 0;

		if (fnoFile.getType().equalsIgnoreCase("Bonus") || fnoFile.getType().equalsIgnoreCase("Split")) {
			System.out.println("ENTER BONUS OR SPLIT ");
			adjustmentValue = fnoFile.getRatioUpperValue() + fnoFile.getRatioLowerValue();
		}

		else if (fnoFile.getType().equalsIgnoreCase("Dividend")) {
			System.out.println("ENTER DIVIDEND");
			adjustmentValue = fnoFile.getAdjustmentValue();
		}

		else if (fnoFile.getType().equalsIgnoreCase("BNS")) {
			// get value from db
			System.out.println("ENTER BNS");
			System.out.println(LocalDate.now());
			System.out.println(fnoFile.getType());

			double bonus_value = Double
					.parseDouble(fnoFileRepository.getAdjustmentValue("bonus", fnoFile.getScrip(), LocalDate.now()));
			System.out.println("hello bonus value" + bonus_value);
			double split_value = Double
					.parseDouble(fnoFileRepository.getAdjustmentValue("split", fnoFile.getScrip(), LocalDate.now()));
			System.out.println(split_value);
			adjustmentValue = bonus_value * split_value;
			System.out.println(adjustmentValue);

		}

		fnoFile.setAdjustmentValue(adjustmentValue);
		fnoFile.setAdjustedLot(fnoFile.getExistedLot() * adjustmentValue);
		fnoFile.setAdjustedStkPrice(fnoFile.getExistedStkPrice() / adjustmentValue);
		fnoFile.setExistedEntryValue(fnoFile.getExistedLot() * fnoFile.getEntryPrice());
		fnoFile.setAdjustedEntryPrice(fnoFile.getExistedEntryValue() / fnoFile.getAdjustedLot());
		fnoFile.setCreatedAt(LocalDate.now());

		FnoFile fno1 = fnoFileRepository.getExitingData(fnoFile.getType(), fnoFile.getScrip(), LocalDate.now());

		if (fno1 != null)
			fnoFile.setId(fno1.getId());

		return fnoFileRepository.save(fnoFile);

	}

}
