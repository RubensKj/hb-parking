package br.com.hbparking.colaborador;

import br.com.hbparking.util.ReadFileCSV;
import br.com.hbparking.vehicleException.ContentDispositionException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class ColaboradorService {
    private final ColaboradorRepository colaboradorRepository;
    private final NotifyHBEmployee notifyHBEmployee;
    private final ReadFileCSV readFileCSV;

    public ColaboradorService(ColaboradorRepository colaboradorRepository, NotifyHBEmployee notifyHBEmployee, ReadFileCSV readFileCSV) {
        this.colaboradorRepository = colaboradorRepository;
        this.notifyHBEmployee = notifyHBEmployee;
        this.readFileCSV = readFileCSV;
    }

    public void save(ColaboradorDTO colaboradorDTO) throws Exception {
        Colaborador colaborador = new Colaborador();
        colaborador.setDataNascimento(convertStringToCalendar(colaboradorDTO.getDataNascimento()));
        colaborador.setEmail(colaboradorDTO.getEmail());
        colaborador.setNome(colaboradorDTO.getNome());
        colaborador.setPcd(colaboradorDTO.isPcd());
        colaborador.setTrabalhoNoturno(colaboradorDTO.isTrabalhoNoturno());

        colaborador = this.colaboradorRepository.save(colaborador);

        //notify hbemployee
        this.notifyHBEmployee.notify("http://localhost:8090/api/teste");
    }

    public Calendar convertStringToCalendar(String dataNascimento) {
        Calendar calendar = Calendar.getInstance();

        //getting informador from string
        String[] data = dataNascimento.split("-");

        int year = Integer.parseInt(data[0]);
        int month = Integer.parseInt(data[1]);
        int day = Integer.parseInt(data[2]);

        calendar.set(year, month, day);

        return calendar;
    }

    public Colaborador getEntityById(int id) {
        Optional<Colaborador> colaboradorOptional = this.colaboradorRepository.findById(id);

        if (colaboradorOptional.isPresent()) {
            return colaboradorOptional.get();
        }
        throw new IllegalArgumentException(String.format("O colaborador informado(%s) não existe", id));
    }

    public void delete(int id) {
        this.colaboradorRepository.deleteById(id);
    }

    public ColaboradorDTO update(ColaboradorDTO colaboradorDTO, int id) {
        Colaborador colaborador = new Colaborador();
        colaborador.setDataNascimento(convertStringToCalendar(colaboradorDTO.getDataNascimento()));
        colaborador.setEmail(colaboradorDTO.getEmail());
        colaborador.setNome(colaboradorDTO.getNome());
        colaborador.setPcd(colaboradorDTO.isPcd());
        colaborador.setTrabalhoNoturno(colaboradorDTO.isTrabalhoNoturno());

        colaborador = this.colaboradorRepository.save(colaborador);

        return ColaboradorDTO.of(colaborador);
    }

    public ColaboradorDTO getColaborador(int id) {
        Optional<Colaborador> colaboradorOptional = this.colaboradorRepository.findById(id);

        if (colaboradorOptional.isPresent()) {
            return ColaboradorDTO.of(colaboradorOptional.get());
        }
        throw new IllegalArgumentException(String.format("O colaborador informado(%s) não existe.", id));
    }

    public void importColaborador(MultipartFile multipartFile) throws Exception {
        List<String[]> data = this.readFileCSV.read(multipartFile);

        for (int i = 0; i < data.size(); i++){
            if(data.get(i).length != 5){throw new ContentDispositionException(String.format("A linha %s não contem todos os dados necessarios, ou contém dados a mais.", i)); }

            List<Integer> linhasErro = new ArrayList<>();

            List<Colaborador> colaboradorList = new ArrayList<>();

            try {
                Colaborador colaborador = new Colaborador();
                colaborador.setNome(data.get(i)[0]);
                colaborador.setEmail(data.get(i)[1]);

                //setting calendar
                String[] calendarSeparated = data.get(i)[2].split("-");

                int year  = Integer.parseInt(calendarSeparated[0]);
                int month = Integer.parseInt(calendarSeparated[1]);
                int day   = Integer.parseInt(calendarSeparated[2]);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day);

                colaborador.setDataNascimento(calendar);

                //convert strings "sim" or "não" to boolean true or false

                boolean[] pcdTrabalhoNoturnoBoolean = this.convertStringBoolean(data.get(i)[3], data.get(i)[4]);

                colaborador.setPcd(pcdTrabalhoNoturnoBoolean[0]);
                colaborador.setTrabalhoNoturno(pcdTrabalhoNoturnoBoolean[1]);

                colaboradorList.add(colaborador);
            }catch (Exception e){
                linhasErro.add(i);
            }

            this.saveAllCsv(colaboradorList);

            //return content dispostion error case any line has an error
            if(linhasErro.size() > 0){
                throw new ContentDispositionException(String.format("As linha %s possui algum erro de disposição de conteudo, verifique o conteudo escrito caso queira cadastra-las," +
                        "as linhas sem erros foram cadastradas com sucesso.", linhasErro));
            }
        }
    }

    public void saveAllCsv(List<Colaborador> colaboradorList) { this.colaboradorRepository.saveAll(colaboradorList); }

    public boolean[] convertStringBoolean(String pcd, String trabalhoNoturno){
        boolean[] pcdTrabalhoNoturnoBoolean = new boolean[2];

        pcdTrabalhoNoturnoBoolean[0] = (pcd.equalsIgnoreCase("sim")) ? true : false;
        pcdTrabalhoNoturnoBoolean[1] = (trabalhoNoturno.equalsIgnoreCase("sim")) ? true : false;

        return pcdTrabalhoNoturnoBoolean;
    }
}
