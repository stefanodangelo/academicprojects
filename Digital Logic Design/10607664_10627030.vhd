library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity project_reti_logiche is
    port(
        i_clk       : in std_logic;
        i_start     : in std_logic;
        i_rst       : in std_logic;
        i_data      : in std_logic_vector(7 downto 0);
        o_address   : out std_logic_vector(15 downto 0);
        o_done      : out std_logic;
        o_en        : out std_logic;
        o_we        : out std_logic;
        o_data      : out std_logic_vector(7 downto 0)
    );
end project_reti_logiche;

architecture Behavioral of project_reti_logiche is
    signal addr0:       std_logic_vector(7 downto 0);
    signal addr1:       std_logic_vector(7 downto 0);
    signal addr2:       std_logic_vector(7 downto 0);
    signal addr3:       std_logic_vector(7 downto 0);
    signal addr4:       std_logic_vector(7 downto 0);
    signal addr5:       std_logic_vector(7 downto 0);
    signal addr6:       std_logic_vector(7 downto 0);
    signal addr7:       std_logic_vector(7 downto 0);
    signal addr8:       std_logic_vector(7 downto 0);
    type S is (RST, IDLE, LOAD0, LOAD1, LOAD2, LOAD3, LOAD4, LOAD5, LOAD6, LOAD7, LOAD8, COMPARE, WRITE, DONE);
    signal current_state, next_state: S;
    begin
        --Delta
        delta: process(i_start, current_state)
        begin 
            case current_state is
                when RST =>
                    next_state <= IDLE;
                when IDLE =>
                    if(i_start = '1') then
                        next_state <= LOAD8;
                    else
                        next_state <= IDLE;
                    end if;
                when LOAD0 =>
                    next_state <= LOAD1;
                when LOAD1 =>
                    next_state <= LOAD2;
                when LOAD2 =>
                    next_state <= LOAD3;
                when LOAD3 =>
                    next_state <= LOAD4;
                when LOAD4 =>
                    next_state <= LOAD5;
                when LOAD5 =>
                    next_state <= LOAD6;
                when LOAD6 =>
                    next_state <= LOAD7;
                when LOAD7 =>
                    next_state <= COMPARE;
                when LOAD8 =>
                    next_state <= LOAD0;
                when COMPARE =>
                    next_state <= WRITE;
                when WRITE =>
                    next_state <= DONE;
                when DONE =>
                    if(i_start = '1') then
                        next_state <= DONE;
                    else
                        next_state <= IDLE;
                    end if;
                when others =>
                    next_state <= RST;
            end case;    
        end process;
        
        --Output
        lambda: process(i_data, addr0, addr1, addr2, addr3, addr4, addr5, addr6, addr7, addr8, current_state)
            begin  
                case current_state is
                    when RST =>
                        o_data <= "00000000";
                        o_en <= '0';
                        o_we <= '0';
                        o_address <= "0000000000000000";
                        o_done <= '0';
                    when IDLE =>
                        o_done <= '0';
                    when LOAD0 =>
                        o_address <= "0000000000000000";
                        o_en <= '1';
                        o_we <= '0';
                        addr0 <= std_logic_vector(signed(addr8) - signed(i_data));
                    when LOAD1 =>
                        o_address <= "0000000000000001";
                        o_en <= '1';
                        o_we <= '0';
                        addr1 <= std_logic_vector(signed(addr8) - signed(i_data));
                    when LOAD2 =>
                        o_address <= "0000000000000010";
                        o_en <= '1';
                        o_we <= '0';
                        addr2 <= std_logic_vector(signed(addr8) - signed(i_data));
                    when LOAD3 =>
                        o_address <= "0000000000000011";
                        o_en <= '1';
                        o_we <= '0';
                        addr3 <= std_logic_vector(signed(addr8) - signed(i_data));
                    when LOAD4 =>
                        o_address <= "0000000000000100";
                        o_en <= '1';
                        o_we <= '0';
                        addr4 <= std_logic_vector(signed(addr8) - signed(i_data));
                    when LOAD5 =>
                        o_address <= "0000000000000101";
                        o_en <= '1';
                        o_we <= '0';
                        addr5 <= std_logic_vector(signed(addr8) - signed(i_data));
                    when LOAD6 =>
                        o_address <= "0000000000000110";
                        o_en <= '1';
                        o_we <= '0';
                        addr6 <= std_logic_vector(signed(addr8) - signed(i_data));
                    when LOAD7 =>
                        o_address <= "0000000000000111";
                        o_en <= '1';
                        o_we <= '0';
                        addr7 <= std_logic_vector(signed(addr8) - signed(i_data));
                    when LOAD8 =>
                        o_address <= "0000000000001000";
                        o_en <= '1';
                        o_we <= '0';
                        addr8 <= i_data;
                    when COMPARE =>
                        --verifico se l'indirizzo appartiene alla WZ_0
                        if(addr0 = "00000000") then
                            o_data <= "1000" & "0001";
                        elsif(addr0 = "00000001") then 
                            o_data <= "1000" & "0010";
                        elsif(addr0 = "00000010") then  
                            o_data <= "1000" & "0100";
                        elsif(addr0 = "00000011") then 
                            o_data <= "1000" & "1000";
                        --verifico se l'indirizzo appartiene alla WZ_1
                        elsif(addr1 = "00000000") then
                            o_data <= "1001" & "0001";
                        elsif(addr1 = "00000001") then 
                            o_data <= "1001" & "0010";
                        elsif(addr1 = "00000010") then  
                            o_data <= "1001" & "0100";
                        elsif(addr1 = "00000011") then 
                            o_data <= "1001" & "1000";
                        --verifico se l'indirizzo appartiene alla WZ_2
                        elsif(addr2 = "00000000") then
                            o_data <= "1010" & "0001";
                        elsif(addr2 = "00000001") then 
                            o_data <= "1010" & "0010";
                        elsif(addr2 = "00000010") then  
                            o_data <= "1010" & "0100";
                        elsif(addr2 = "00000011") then 
                            o_data <= "1010" & "1000";
                        --verifico se l'indirizzo appartiene alla WZ_3
                        elsif(addr3 = "00000000") then
                            o_data <= "1011" & "0001";
                        elsif(addr3 = "00000001") then 
                            o_data <= "1011" & "0010";
                        elsif(addr3 = "00000010") then  
                            o_data <= "1011" & "0100";
                        elsif(addr3 = "00000011") then 
                            o_data <= "1011" & "1000";
                        --verifico se l'indirizzo appartiene alla WZ_4
                        elsif(addr4 = "00000000") then
                            o_data <= "1100" & "0001";
                        elsif(addr4 = "00000001") then 
                            o_data <= "1100" & "0010";
                        elsif(addr4 = "00000010") then  
                            o_data <= "1100" & "0100";
                        elsif(addr4 = "00000011") then 
                            o_data <= "1100" & "1000";
                        --verifico se l'indirizzo appartiene alla WZ_5
                        elsif(addr5 = "00000000") then
                            o_data <= "1101" & "0001";
                        elsif(addr5 = "00000001") then 
                            o_data <= "1101" & "0010";
                        elsif(addr5 = "00000010") then  
                            o_data <= "1101" & "0100";
                        elsif(addr5 = "00000011") then 
                            o_data <= "1101" & "1000";
                        --verifico se l'indirizzo appartiene alla WZ_6
                        elsif(addr6 = "00000000") then
                            o_data <= "1110" & "0001";
                        elsif(addr6 = "00000001") then 
                            o_data <= "1110" & "0010";
                        elsif(addr6 = "00000010") then  
                            o_data <= "1110" & "0100";
                        elsif(addr6 = "00000011") then 
                            o_data <= "1110" & "1000";
                        --verifico se l'indirizzo appartiene alla WZ_7
                        elsif(addr7 = "00000000") then
                            o_data <= "1111" & "0001";
                        elsif(addr7 = "00000001") then 
                            o_data <= "1111" & "0010";
                        elsif(addr7 = "00000010") then  
                            o_data <= "1111" & "0100";
                        elsif(addr7 = "00000011") then 
                            o_data <= "1111" & "1000"; 
                        --l'indirizzo non appartiene a nessuna WZ
                        else
                            o_data <= addr8;
                        end if;
                    when WRITE => 
                        o_address <= "0000000000001001";
                        o_en <= '1';
                        o_we <= '1';
                    when DONE =>
                        o_done <= '1';
                    when others => 
                        o_data <= "00000000";
                        o_en <= '0';
                        o_we <= '0';
                        o_address <= "0000000000000000";
                        o_done <= '0';
                end case;
        end process; 
        
         --Stato
        state: process(i_clk, i_rst)
        begin
            if(i_rst = '1') then
                current_state <= RST;
            elsif(falling_edge(i_clk)) then
                current_state <= next_state;
            end if;
        end process;
end Behavioral;