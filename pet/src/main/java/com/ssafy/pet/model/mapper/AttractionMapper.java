package com.ssafy.pet.model.mapper;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.pet.dto.AttractionsDto;

public interface AttractionMapper {
	//지역 코드로 검색
		List<AttractionsDto> searchByCode(int area_code) throws SQLException;
		
		//지역 코드와 키워드로 검색
		List<AttractionsDto> searchByAreaCodeAndKeyword(int area_code, String keyword) throws SQLException;
		
		//관광지 유형과 키워드로 검색
		List<AttractionsDto> searchByTypeAndKeyword(int content_type_id, String keyword) throws SQLException;
		
		//지역 이름으로 검색
		List<AttractionsDto> searchByAreaName(String area_name) throws SQLException;
		
		//지역 코드와 관광지 유형으로 검색
		List<AttractionsDto> searchAreaCodeAndType(int area_code, int content_type_id) throws SQLException;
		
		//모든 검색조건을 이용해서 검색
		List<AttractionsDto> search(int area_code, int content_type_id, String keyword) throws SQLException;
		
		//이름과 일치하는 관광지명 || 주소 검색
		List<AttractionsDto> searchByKeyword(String keyword) throws SQLException;
		
		//관광지 유형 검색
		List<AttractionsDto> searchByContentId(int content_type_id) throws SQLException;
		
		//지역코드와 관광지유형으로 검색
		List<AttractionsDto> searchByCodeWithContentId(int area_code, int content_type_id) throws SQLException;
		
		void addHotplace(int content_id) throws SQLException;
		List<AttractionsDto> viewHotplaces() throws SQLException;
		public boolean checkContentIdExists(int content_id) throws SQLException;
}
