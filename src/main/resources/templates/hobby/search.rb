# coding: utf-8

SEARCH_DATE = ARGV[0]

FILE_NAME = "C:\\Users\\hsakuchi\\norewritebatch\\search" + "#{SEARCH_DATE}" + ".bat"
LOG_DIR = "C:/Users/hsakuchi/memo/diary/hobby/"


File::open("#{FILE_NAME}", "w+") do |f|
  f.puts("@echo off")
  f.puts("set word1=%1")
  1.upto(31) do |i|
	days = sprintf("%02d", i)
	log_file_name = "log" + "#{SEARCH_DATE}" + "#{days.to_s}" + ".txt"
  	f.puts("find /n \"%word1%\" #{log_file_name} ")  
  end  
end
