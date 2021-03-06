package br.com.hbparking.colaborador;

import br.com.hbparking.file.FileNotSupportedException;
import br.com.hbparking.security.role.Role;
import br.com.hbparking.security.role.RoleName;
import br.com.hbparking.security.role.RoleService;
import br.com.hbparking.security.user.User;
import br.com.hbparking.security.user.UserDTO;
import br.com.hbparking.security.user.UserService;
import br.com.hbparking.util.DateHelper;
import br.com.hbparking.util.ReadFileCSV;
import br.com.hbparking.vehicleException.ContentDispositionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ColaboradorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ColaboradorService.class);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final ColaboradorRepository colaboradorRepository;
    private final NotifyHBEmployee notifyHBEmployee;
    private final ReadFileCSV readFileCSV;
    private final UserService userService;
    private final RoleService roleService;

    public ColaboradorService(ColaboradorRepository colaboradorRepository, NotifyHBEmployee notifyHBEmployee, ReadFileCSV readFileCSV, UserService userService, RoleService roleService) {
        this.colaboradorRepository = colaboradorRepository;
        this.notifyHBEmployee = notifyHBEmployee;
        this.readFileCSV = readFileCSV;
        this.userService = userService;
        this.roleService = roleService;
    }

    public ColaboradorDTO save(ColaboradorDTO colaboradorDTO) throws EmailAlreadyExistsException {
        this.validate(colaboradorDTO);

        Colaborador colaborador = new Colaborador();

        LocalDate localDate = colaboradorDTO.getDataNascimento();
        formatter.format(localDate);
        colaborador.setDataNascimento(localDate);
        colaborador.setEmail(colaboradorDTO.getEmail());
        colaborador.setNome(colaboradorDTO.getNome());
        colaborador.setPcd(colaboradorDTO.isPcd());
        colaborador.setTrabalhoNoturno(colaboradorDTO.isTrabalhoNoturno());
        colaborador.setResideForaBlumenau(colaboradorDTO.isResideForaBlumenau());
        colaborador.setOfereceCarona(colaboradorDTO.isOfereceCarona());

        ColaboradorDTO colaboradorSalvoDTO = ColaboradorDTO.of(this.colaboradorRepository.save(colaborador));

        List<RoleName> roleNameList = new ArrayList<>();
        roleNameList.add(RoleName.ROLE_USER);

        System.out.println(DateHelper.formatDateToPassword(colaboradorDTO.getDataNascimento()) + " DATA NASCIMENTO COM FORMAT");
        this.userService.save(new UserDTO(colaboradorDTO.getEmail(), colaboradorDTO.getNome(), this.userService.encryptUserDTOPassword(DateHelper.formatDateToPassword(colaboradorDTO.getDataNascimento())), roleNameList));

        //notify hbemployee
        new Thread(() -> {
            try {
                this.notifyHBEmployee.notify("http://localhost:8090/api/teste");
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }).start();
        return colaboradorSalvoDTO;
    }

    private void validate(ColaboradorDTO colaboradorDTO) throws EmailAlreadyExistsException {
        LOGGER.info("Validando ColaboradorDTO");
        LOGGER.debug("ColaboradorDTO: {}", colaboradorDTO);

        if (StringUtils.isEmpty(colaboradorDTO.getEmail())) {
            throw new IllegalArgumentException("E-mail não pode ser nulo/vazio.");
        }

        if (this.colaboradorRepository.existsByEmail(colaboradorDTO.getEmail())) {
            throw new EmailAlreadyExistsException("E-mail já existe no banco, não podendo criar com o colaborador com esse e-mail.");
        }

        if (StringUtils.isEmpty(colaboradorDTO.getNome())) {
            throw new IllegalArgumentException("Nome do colaborador não pode ser nulo/vazio");
        }

        if (colaboradorDTO.getDataNascimento().isEqual(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }

        if (colaboradorDTO.getDataNascimento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }

        if (colaboradorDTO.getDataNascimento().isBefore(LocalDate.of(1900, 12, 31))) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }
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
        LocalDate localDate = colaboradorDTO.getDataNascimento();
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

    public void importColaborador(MultipartFile multipartFile) throws IOException, FileNotSupportedException, ContentDispositionException {
        List<String[]> data = this.readFileCSV.read(multipartFile);
        List<Colaborador> colaboradorList = new ArrayList<>();
        List<User> usersList = new ArrayList<>();

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(this.roleService.findRoleByName(RoleName.ROLE_USER));
        List<Integer> linhasErro = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).length < 5) {
                throw new ContentDispositionException(String.format("A linha %s não contem todos os dados necessarios, ou contém dados a mais.", i));
            }

            LocalDate localDate = DateHelper.formatDateFromCSVToLocalDate(data.get(i)[2].split("/"));
            ColaboradorDTO colaboradorDTO = new ColaboradorDTO(data.get(i)[1], data.get(i)[0], localDate, this.convertStringToBoolean(data.get(i)[3]), this.convertStringToBoolean(data.get(i)[4]));
            try {
                this.validate(colaboradorDTO);
            } catch (EmailAlreadyExistsException e) {
                linhasErro.add((i + 2));
                continue;
            }


            Colaborador colaborador = new Colaborador();
            colaborador.setNome(colaboradorDTO.getNome());
            colaborador.setEmail(colaboradorDTO.getEmail());
            colaborador.setDataNascimento(colaboradorDTO.getDataNascimento());
            colaborador.setPcd(colaboradorDTO.isPcd());
            colaborador.setTrabalhoNoturno(colaboradorDTO.isTrabalhoNoturno());

            User user = new User(
                    colaborador.getEmail(),
                    colaborador.getNome(),
                    this.userService.encryptUserDTOPassword(DateHelper.formatDateToPassword(colaborador.getDataNascimento())),
                    roleSet
            );

            usersList.add(user);
            colaboradorList.add(colaborador);
        }

        this.saveAllCsv(colaboradorList);
        this.userService.saveAllUsers(usersList);

        //return content dispostion error case any line has an error
        if (!linhasErro.isEmpty()) {
            throw new ContentDispositionException(String.format("As linha %s possui algum erro de disposição de conteudo, verifique o conteudo escrito caso queira cadastra-las," +
                    "as linhas sem erros foram cadastradas com sucesso.", linhasErro));
        }
    }

    public void saveAllCsv(List<Colaborador> colaboradorList) {
        this.colaboradorRepository.saveAll(colaboradorList);
    }

    public Colaborador findById(Long id) {
        Optional<Colaborador> colaborador = this.colaboradorRepository.findById(id);
        if (colaborador.isPresent()) {
            return colaborador.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public boolean convertStringToBoolean(String afirmacao) {
        return afirmacao.equalsIgnoreCase("sim") || afirmacao.equalsIgnoreCase("s");
    }

    public ColaboradorDTO receberParametrosLocacao(ColaboradorDTO colaboradorDTO) {
        Colaborador colaborador = this.getEntityById(colaboradorDTO.getId());

        colaborador.setOfereceCarona(colaboradorDTO.isOfereceCarona());
        colaborador.setResideForaBlumenau(colaboradorDTO.isResideForaBlumenau());

        return ColaboradorDTO.of(this.colaboradorRepository.save(colaborador));
    }
}
