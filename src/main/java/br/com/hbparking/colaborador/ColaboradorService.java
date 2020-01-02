package br.com.hbparking.colaborador;

import br.com.hbparking.security.role.Role;
import br.com.hbparking.security.role.RoleName;
import br.com.hbparking.security.role.RoleService;
import br.com.hbparking.security.user.User;
import br.com.hbparking.security.user.UserDTO;
import br.com.hbparking.security.user.UserService;
import br.com.hbparking.util.ReadFileCSV;
import br.com.hbparking.vehicleException.ContentDispositionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ColaboradorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ColaboradorService.class);

    private final ColaboradorRepository colaboradorRepository;
    private final NotifyHBEmployee notifyHBEmployee;
    private final ReadFileCSV readFileCSV;
    private final UserService userService;
    private final RoleService roleService;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ColaboradorService(ColaboradorRepository colaboradorRepository, NotifyHBEmployee notifyHBEmployee, ReadFileCSV readFileCSV, UserService userService, RoleService roleService) {
        this.colaboradorRepository = colaboradorRepository;
        this.notifyHBEmployee = notifyHBEmployee;
        this.readFileCSV = readFileCSV;
        this.userService = userService;
        this.roleService = roleService;
    }

    public void save(ColaboradorDTO colaboradorDTO) {
        Colaborador colaborador = new Colaborador();

        LocalDate localDate = LocalDate.parse(colaboradorDTO.getDataNascimento());
        formatter.format(localDate);
        colaborador.setDataNascimento(localDate);
        colaborador.setEmail(colaboradorDTO.getEmail());
        colaborador.setNome(colaboradorDTO.getNome());
        colaborador.setPcd(colaboradorDTO.isPcd());
        colaborador.setTrabalhoNoturno(colaboradorDTO.isTrabalhoNoturno());
        colaborador.setResideForaBlumenau(colaboradorDTO.isResideForaBlumenau());
        colaborador.setOfereceCarona(colaboradorDTO.isOfereceCarona());

        List<RoleName> roleNameList = new ArrayList<>();
        roleNameList.add(RoleName.ROLE_USER);

        this.userService.save(new UserDTO(colaboradorDTO.getEmail(), colaboradorDTO.getNome(), this.userService.encryptUserDTOPassword(colaboradorDTO.getDataNascimento().replace("-", "")), roleNameList));

        //notify hbemployee
        new Thread(() -> {
            try {
                this.notifyHBEmployee.notify("http://localhost:8090/api/teste");
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }).start();
    }

    public Colaborador getEntityById(Long id) {
        Optional<Colaborador> colaboradorOptional = this.colaboradorRepository.findById(id);

        if (colaboradorOptional.isPresent()) {
            return colaboradorOptional.get();
        }
        throw new IllegalArgumentException(String.format("O colaborador informado(%s) não existe", id));
    }

    public void delete(Long id) {
        this.colaboradorRepository.deleteById(id);
    }

    public ColaboradorDTO update(ColaboradorDTO colaboradorDTO, Long id) {
        Colaborador colaborador = this.getEntityById(id);
        LocalDate localDate = LocalDate.parse(colaboradorDTO.getDataNascimento());
        formatter.format(localDate);
        colaborador.setDataNascimento(localDate);
        colaborador.setEmail(colaboradorDTO.getEmail());
        colaborador.setNome(colaboradorDTO.getNome());
        colaborador.setPcd(colaboradorDTO.isPcd());
        colaborador.setTrabalhoNoturno(colaboradorDTO.isTrabalhoNoturno());

        colaborador = this.colaboradorRepository.save(colaborador);

        return ColaboradorDTO.of(colaborador);
    }

    public ColaboradorDTO getColaborador(Long id) {
        Optional<Colaborador> colaboradorOptional = this.colaboradorRepository.findById(id);

        if (colaboradorOptional.isPresent()) {
            return ColaboradorDTO.of(colaboradorOptional.get());
        }
        throw new IllegalArgumentException(String.format("O colaborador informado(%s) não existe.", id));
    }

    public void importColaborador(MultipartFile multipartFile) throws Exception {
        List<String[]> data = this.readFileCSV.read(multipartFile);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(this.roleService.findRoleByName(RoleName.ROLE_USER));
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).length != 5) {
                throw new ContentDispositionException(String.format("A linha %s não contem todos os dados necessarios, ou contém dados a mais.", i));
            }

            List<Integer> linhasErro = new ArrayList<>();

            List<Colaborador> colaboradorList = new ArrayList<>();
            List<User> usersList = new ArrayList<>();

            try {
                Colaborador colaborador = new Colaborador();
                colaborador.setNome(data.get(i)[0]);
                colaborador.setEmail(data.get(i)[1]);

                //setting calendar
                String[] calendarSeparated = data.get(i)[2].split("-");

                int year = Integer.parseInt(calendarSeparated[0]);
                int month = Integer.parseInt(calendarSeparated[1]);
                int day = Integer.parseInt(calendarSeparated[2]);

                LocalDate localDate = LocalDate.of(year, month, day);
                formatter.format(localDate);

                colaborador.setDataNascimento(localDate);

                //convert strings "sim" or "não" to boolean true or false

                colaborador.setPcd(this.convertStringToBoolean(data.get(i)[3]));
                colaborador.setTrabalhoNoturno(this.convertStringToBoolean(data.get(i)[4]));

                User user = new User(
                        data.get(i)[1],
                        data.get(i)[0],
                        this.userService.encryptUserDTOPassword(localDate.toString().replace("-", "")),
                        roleSet
                );


                usersList.add(user);
                colaboradorList.add(colaborador);
            } catch (Exception e) {
                linhasErro.add(i);
            }

            this.saveAllCsv(colaboradorList);
            this.userService.saveAllUsers(usersList);

            //return content dispostion error case any line has an error
            if (linhasErro.isEmpty()) {
                throw new ContentDispositionException(String.format("As linha %s possui algum erro de disposição de conteudo, verifique o conteudo escrito caso queira cadastra-las," +
                        "as linhas sem erros foram cadastradas com sucesso.", linhasErro));
            }
        }
    }

    public void saveAllCsv(List<Colaborador> colaboradorList) {
        this.colaboradorRepository.saveAll(colaboradorList);
    }

    public boolean[] convertStringBoolean(String pcd, String trabalhoNoturno) {
        boolean[] pcdTrabalhoNoturnoBoolean = new boolean[2];

        pcdTrabalhoNoturnoBoolean[0] = pcd.equalsIgnoreCase("sim");
        pcdTrabalhoNoturnoBoolean[1] = trabalhoNoturno.equalsIgnoreCase("sim");

        return pcdTrabalhoNoturnoBoolean;
    }


    public Colaborador findById(Long id) {
        Optional<Colaborador> colaborador = this.colaboradorRepository.findById(id);
        if (colaborador.isPresent()) {
            return colaborador.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public boolean convertStringToBoolean(String afirmacao) {
        return afirmacao.equalsIgnoreCase("sim");
    }

    public ColaboradorDTO receberParametrosLocacao(ColaboradorDTO colaboradorDTO) {
        Colaborador colaborador = this.getEntityById(colaboradorDTO.getId());

        colaborador.setOfereceCarona(colaboradorDTO.isOfereceCarona());
        colaborador.setResideForaBlumenau(colaboradorDTO.isResideForaBlumenau());

        return ColaboradorDTO.of(this.colaboradorRepository.save(colaborador));
    }

    public Colaborador findByEmail(String email) throws CannotFindColaborador {
        return this.colaboradorRepository.findByEmail(email).orElseThrow(() ->
                new CannotFindColaborador("O colaborador do email: "+ email +" não existe"));
    }


}
